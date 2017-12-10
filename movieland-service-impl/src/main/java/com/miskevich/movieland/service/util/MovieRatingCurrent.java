package com.miskevich.movieland.service.util;

import com.miskevich.movieland.dao.jdbc.JdbcMovieDao;
import com.miskevich.movieland.entity.Movie;
import com.miskevich.movieland.model.MovieRating;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

@Service
public class MovieRatingCurrent {
    private final Logger LOG = LoggerFactory.getLogger(getClass());
    private Map<Integer, MovieRating> actualMovieRating = new HashMap<>();
    private final ReadWriteLock LOCK = new ReentrantReadWriteLock();

    @Autowired
    private JdbcMovieDao jdbcMovieDao;

    @PostConstruct
    private void initCurrentRatings() {
        List<MovieRating> movieRatings = jdbcMovieDao.getAllMoviesWithRatings();
        for (MovieRating movieRating : movieRatings) {
            actualMovieRating.put(movieRating.getMovie().getId(), movieRating);
            LOG.debug("Valid movie rating for current usage with votes {}", movieRating);
        }
        LOG.info("Valid movie ratings for current usage were initialized");
    }

    public Movie refreshRating(MovieRating userRating) {
        int movieId = userRating.getMovie().getId();
        MovieRating currentMovieRating;
        double currentRating = 0;
        long currentVotesCount = 0;
        double newRating;
        Lock readLock = LOCK.readLock();
        Lock writeLock = LOCK.writeLock();

        try {
            readLock.lock();
            currentMovieRating = actualMovieRating.get(movieId);
            if (currentMovieRating != null) {
                currentRating = currentMovieRating.getRating();
                currentVotesCount = currentMovieRating.getVotes();
            }
        } finally {
            readLock.unlock();
        }

        try {
            writeLock.lock();
            long newVotesCount = currentVotesCount + 1;
            newRating = RateCalculator.calculate(userRating, currentRating, newVotesCount);

            if (currentMovieRating == null) {
                currentMovieRating = new MovieRating();
            }
            currentMovieRating.setVotes(newVotesCount);
            currentMovieRating.setRating(newRating);

            actualMovieRating.put(movieId, currentMovieRating);
        } finally {
            writeLock.unlock();
        }

        Movie movie = new Movie();
        movie.setId(movieId);
        movie.setRating(newRating);
        return movie;
    }

    void invalidate(List<MovieRating> movieRatings) {
        Map<Integer, MovieRating> newActualMovieRating = new HashMap<>();
        for (MovieRating movieRating : movieRatings) {
            newActualMovieRating.put(movieRating.getMovie().getId(), movieRating);
        }

        Lock writeLock = LOCK.writeLock();
        try {
            writeLock.lock();
            actualMovieRating = newActualMovieRating;
        } finally {
            writeLock.unlock();
        }
    }

    public double getRatingByMovieId(int id) {
        MovieRating movieRating = actualMovieRating.get(id);
        return movieRating.getRating();
    }
}
