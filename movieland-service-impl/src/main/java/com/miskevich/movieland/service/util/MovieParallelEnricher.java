package com.miskevich.movieland.service.util;

import com.miskevich.movieland.entity.Movie;
import com.miskevich.movieland.service.ICountryService;
import com.miskevich.movieland.service.IGenreService;
import com.miskevich.movieland.service.IReviewService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.*;

@Service
public class MovieParallelEnricher {
    private final Logger LOG = LoggerFactory.getLogger(getClass());
    private ExecutorService executorService = Executors.newCachedThreadPool();
    private ThreadPoolExecutor executor = (ThreadPoolExecutor) executorService;
    @Autowired
    private IGenreService genreService;
    @Autowired
    private ICountryService countryService;
    @Autowired
    private IReviewService reviewService;
    @Value("${executor.max.pool.size}")
    private int maxPoolSize;
    @Value("${executor.core.pool.size}")
    private int corePoolSize;
    @Value("${executor.enrichment.task.timeout}")
    private long enrichmentTaskTimeout;

    @PostConstruct
    private void init() {
        executor.setMaximumPoolSize(maxPoolSize);
        executor.setCorePoolSize(corePoolSize);
    }

    @PreDestroy
    private void destroy() {
        executorService.shutdownNow();
    }

    public void enrich(Movie movie, boolean isFullEnrichmentRequired) {
        List<Callable<Integer>> tasks = new ArrayList<>();
        if (isFullEnrichmentRequired) {
            Callable<Integer> genreTask = () -> {genreService.enrichWithGenre(movie); return 1;};
            Callable<Integer> countryTask = () -> {countryService.enrichWithCountry(movie); return 1;};
            Callable<Integer> reviewTask = () -> {reviewService.enrichWithReview(movie); return 1;};

            tasks.add(genreTask);
            tasks.add(countryTask);
            tasks.add(reviewTask);
        } else {
            Callable<Integer> genreTask = () -> {genreService.enrichWithGenre(movie); return 1;};
            Callable<Integer> countryTask = () -> {countryService.enrichWithCountry(movie); return 1;};

            tasks.add(genreTask);
            tasks.add(countryTask);
        }

        try {
            List<Future<Integer>> futures = executorService.invokeAll(tasks, enrichmentTaskTimeout, TimeUnit.MINUTES);
            for (Future<Integer> future : futures) {
                if (future.isCancelled()) {
                    LOG.warn("Enrichment task was cancelled by timeout");
                }
            }
        } catch (InterruptedException e) {
            LOG.warn("Movie enrichment wasn't completed");
        }
    }
}
