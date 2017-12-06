package com.miskevich.movieland.service.util;

import com.miskevich.movieland.dao.jdbc.JdbcMovieDao;
import com.miskevich.movieland.model.MovieRating;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.UncategorizedSQLException;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.SQLException;
import java.util.List;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

@Service
public class MovieRatingBuffer {
    private final Logger LOG = LoggerFactory.getLogger(getClass());
    private final Queue<MovieRating> MOVIE_RATING_BUFFER = new ConcurrentLinkedQueue<>();
    @Autowired
    private JdbcMovieDao jdbcMovieDao;
    @Autowired
    private MovieRatingCurrent movieRatingCurrent;

    public void add(MovieRating movieRating){
        MOVIE_RATING_BUFFER.add(movieRating);
        LOG.info("MovieRating {} was added into buffer", movieRating);
    }

    @Scheduled(initialDelayString = "${init.delay.movie.rating.buffer}", fixedRateString = "${fixed.rate.movie.rating.buffer}")
    @Transactional(rollbackFor = UncategorizedSQLException.class)
    void invalidate(){
        while (!MOVIE_RATING_BUFFER.isEmpty()){
            jdbcMovieDao.rate(MOVIE_RATING_BUFFER.poll());
        }
        List<MovieRating> movieRatings = jdbcMovieDao.calculateRatings();
        jdbcMovieDao.persistRatingsAndVotes(movieRatings);
        movieRatingCurrent.invalidate(movieRatings);
    }
}
