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
import java.util.Collection;
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

    public void enrich(Movie movie, boolean isFullEnrichmentRequired) {
        List<Runnable> tasks = new ArrayList<>();

        Runnable genreTask = () -> genreService.enrichWithGenre(movie);
        Runnable countryTask = () -> countryService.enrichWithCountry(movie);
        tasks.add(genreTask);
        tasks.add(countryTask);

        if (isFullEnrichmentRequired) {
            Runnable reviewTask = () -> reviewService.enrichWithReview(movie);
            tasks.add(reviewTask);
        }

        List<Callable<Object>> qqq = new ArrayList<>();
        for (Runnable task : tasks){
            Callable<Object> callable = Executors.callable(task);
            qqq.add(callable);
        }

        try {
            List<Future<Object>> futures = executor.invokeAll(qqq, enrichmentTaskTimeout, TimeUnit.MINUTES);
            for (Future<Object> future : futures) {
                if (future.isCancelled()) {
                    LOG.warn("Enrichment task was cancelled by timeout");
                }
            }
        } catch (InterruptedException e) {
            LOG.warn("Movie enrichment wasn't completed");
        }
    }


    static class MyTask implements Callable {

        @Override
        public Object call() throws Exception {
            System.out.println("start");
            Thread.sleep(1000);
            System.out.println("111");
            return null;
        }
    }

    public static void main(String[] args) throws InterruptedException {
        MyTask myTask = new MyTask();
        MyTask myTask2 = new MyTask();
        List<Callable<MyTask>> list = new ArrayList<>();
        list.add(myTask);
        list.add(myTask2);
        executor.invokeAll(list, 500, TimeUnit.MILLISECONDS);
    }
}
