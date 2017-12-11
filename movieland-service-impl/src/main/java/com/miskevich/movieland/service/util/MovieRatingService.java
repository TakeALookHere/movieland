package com.miskevich.movieland.service.util;

import com.miskevich.movieland.dao.IMovieDao;
import com.miskevich.movieland.dao.jdbc.JdbcMovieDao;
import com.miskevich.movieland.model.MovieRating;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

@Service
public class MovieRatingService {
    private final Logger LOG = LoggerFactory.getLogger(getClass());
    private final Queue<MovieRating> MOVIE_RATING_BUFFER = new ConcurrentLinkedQueue<>();
    @Autowired
    private IMovieDao iMovieDao;
    @Autowired
    private MovieRatingCurrent movieRatingCurrent;

    public void add(MovieRating movieRating) {
        MOVIE_RATING_BUFFER.add(movieRating);
        LOG.info("MovieRating {} was added into buffer", movieRating);
    }

    @Scheduled(initialDelayString = "${init.delay.movie.rating.buffer}", fixedRateString = "${fixed.rate.movie.rating.buffer}")
    @Transactional
    public void invalidate() {
        List<MovieRating> movieRatingsForBatchUpdate = new ArrayList<>();
        while (!MOVIE_RATING_BUFFER.isEmpty()) {
            movieRatingsForBatchUpdate.add(MOVIE_RATING_BUFFER.poll());
        }

        iMovieDao.rate(movieRatingsForBatchUpdate);
        List<MovieRating> movieRatings = iMovieDao.calculateRatings();
        iMovieDao.persistRatingsAndVotes(movieRatings);
        movieRatingCurrent.invalidate(movieRatings);
    }
}
