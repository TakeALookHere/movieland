package com.miskevich.movieland.service.util;

import com.miskevich.movieland.entity.Country;
import com.miskevich.movieland.entity.Genre;
import com.miskevich.movieland.entity.Movie;
import com.miskevich.movieland.entity.Review;
import com.miskevich.movieland.service.ICountryService;
import com.miskevich.movieland.service.IGenreService;
import com.miskevich.movieland.service.IReviewService;
import com.miskevich.movieland.service.model.EnrichmentType;
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
    private static ThreadPoolExecutor executor = (ThreadPoolExecutor) Executors.newCachedThreadPool();
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
        executor.shutdown();
    }

    @SuppressWarnings("unchecked")
    public void enrich(Movie movie, EnrichmentType enrichmentType) {
        List<Callable<List<?>>> callables = new ArrayList<>();

        Callable<List<?>> genres = () -> genreService.getByMovieId(movie.getId());
        Callable<List<?>> countries = () -> countryService.getByMovieId(movie.getId());
        callables.add(genres);
        callables.add(countries);

        if (enrichmentType.equals(EnrichmentType.FULL)) {
            Callable<List<?>> reviews = () -> reviewService.getByMovieId(movie.getId());
            callables.add(reviews);
        }

        try {
            List<Future<List<?>>> futures = executor.invokeAll(callables, enrichmentTaskTimeout, TimeUnit.MILLISECONDS);
            for (Future<List<?>> future : futures) {
                if (!future.isCancelled()) {
                    List<?> objects = future.get();

                    if(!objects.isEmpty()){
                        Class<?> aClass = objects.get(0).getClass();
                        if (aClass.getCanonicalName().equals(Genre.class.getCanonicalName())) {
                            movie.setGenres((List<Genre>) objects);
                        } else if (aClass.getCanonicalName().equals(Country.class.getCanonicalName())) {
                            movie.setCountries((List<Country>) objects);
                        } else {
                            movie.setReviews((List<Review>) objects);
                        }
                    }
                } else {
                    LOG.warn("Enrichment task was cancelled by timeout");
                }
            }
        } catch (InterruptedException | ExecutionException e) {
            LOG.warn("Movie enrichment wasn't completed");
        }
    }
}
