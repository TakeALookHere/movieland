package com.miskevich.movieland.service.cache;

import com.miskevich.movieland.entity.Movie;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.lang.ref.SoftReference;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class MovieCache {

    private final Logger LOG = LoggerFactory.getLogger(getClass());
    private final static Map<Integer, SoftReference<Movie>> MOVIE_CACHE = new ConcurrentHashMap<>();

    public Optional<Movie> get(int id) {
        long startTime = System.currentTimeMillis();
        SoftReference<Movie> softReference = MOVIE_CACHE.get(id);
        if (softReference == null) {
            return Optional.empty();
        }
        Movie movie = softReference.get();
        LOG.info("Movie was received from cache {}", movie);
        LOG.info("It took {} ms", System.currentTimeMillis() - startTime);
        return Optional.ofNullable(movie);
    }

    public void add(Movie movie) {
        SoftReference<Movie> softReference = MOVIE_CACHE.put(movie.getId(), new SoftReference<>(movie));
        if (softReference == null) {
            LOG.info("Movie was added into cache {}", movie);
        } else {
            Movie oldMovie = softReference.get();
            LOG.info("OldMovie {} Was replaced in cache by NewMovie {}", oldMovie, movie);
        }
    }
}
