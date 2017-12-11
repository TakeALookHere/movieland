package com.miskevich.movieland.service.util;

import com.miskevich.movieland.dao.IMovieDao;
import com.miskevich.movieland.entity.Movie;
import com.miskevich.movieland.model.MovieRating;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class MovieRatingCurrent {
    private final Logger LOG = LoggerFactory.getLogger(getClass());
    private volatile Map<Integer, MovieRating> actualMovieRating = new ConcurrentHashMap<>();

    @Autowired
    private IMovieDao iMovieDao;

    @PostConstruct
    private void initCurrentRatings() {
        List<MovieRating> movieRatings = iMovieDao.getAllMoviesWithRatings();
        for (MovieRating movieRating : movieRatings) {
            actualMovieRating.put(movieRating.getMovie().getId(), movieRating);
            LOG.debug("Valid movie rating for current usage with votes {}", movieRating);
        }
        LOG.info("Valid movie ratings for current usage were initialized");
    }

    public Movie refreshRating(MovieRating userRating) {
        int movieId = userRating.getMovie().getId();
        double currentRating = 0;
        long currentVotesCount = 0;

        MovieRating currentMovieRating = actualMovieRating.get(movieId);
        if (currentMovieRating != null) {
            currentRating = currentMovieRating.getRating();
            currentVotesCount = currentMovieRating.getVotes();
        }

        long newVotesCount = currentVotesCount + 1;
        double newRating = RateCalculator.calculate(userRating, currentRating, newVotesCount);

        if (currentMovieRating == null) {
            currentMovieRating = new MovieRating();
        }
        currentMovieRating.setVotes(newVotesCount);
        currentMovieRating.setRating(newRating);

        actualMovieRating.put(movieId, currentMovieRating);

        Movie movie = new Movie();
        movie.setId(movieId);
        movie.setRating(newRating);
        return movie;
    }

    void invalidate(List<MovieRating> movieRatings) {
        Map<Integer, MovieRating> newActualMovieRating = new ConcurrentHashMap<>();
        for (MovieRating movieRating : movieRatings) {
            newActualMovieRating.put(movieRating.getMovie().getId(), movieRating);
        }

        actualMovieRating = newActualMovieRating;
    }

    public double getRatingByMovieId(int id) {
        MovieRating movieRating = actualMovieRating.get(id);
        return movieRating.getRating();
    }
}
