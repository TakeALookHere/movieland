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

    public void enrich(Movie movie, EnrichmentType enrichmentType) {
        List<Callable<List<?>>> callables = new ArrayList<>();

        Callable<List<?>> genreTask = () -> genreService.getByMovieId(movie.getId());
        Callable<List<?>> countryTask = () -> countryService.getByMovieId(movie.getId());
        callables.add(genreTask);
        callables.add(countryTask);

        if (enrichmentType.equals(EnrichmentType.FULL)) {
            Callable<List<?>> reviewTask = () -> reviewService.getByMovieId(movie.getId());
            callables.add(reviewTask);
        }

        enrichByTaskResults(movie, enrichmentType, callables);
    }

    @SuppressWarnings("unchecked")
    private void enrichByTaskResults(Movie movie, EnrichmentType enrichmentType, List<Callable<List<?>>> callables) {
        try {
            List<Future<List<?>>> futures = executor.invokeAll(callables, enrichmentTaskTimeout, TimeUnit.MINUTES);

            Future<List<?>> genreFuture = futures.get(0);
            if(!genreFuture.isCancelled()){
                movie.setGenres((List<Genre>) genreFuture);
            }else {
                LOG.warn("Enrichment task for genres was cancelled by timeout");
            }

            Future<List<?>> countryFuture = futures.get(1);
            if(!countryFuture.isCancelled()){
                movie.setCountries((List<Country>) countryFuture);
            }else {
                LOG.warn("Enrichment task for countries was cancelled by timeout");
            }

            if(enrichmentType.equals(EnrichmentType.FULL)){
                Future<List<?>> reviewFuture = futures.get(2);
                if(!reviewFuture.isCancelled()){
                    movie.setReviews((List<Review>) reviewFuture);
                }else {
                    LOG.warn("Enrichment task for reviews was cancelled by timeout");
                }
            }
        } catch (InterruptedException e) {
            LOG.warn("Movie enrichment wasn't fully completed");
        }
    }
}
