package com.miskevich.movieland.dao.jdbc;

import com.miskevich.movieland.dao.IMovieDao;
import com.miskevich.movieland.dao.jdbc.mapper.MovieRowMapper;
import com.miskevich.movieland.entity.Movie;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class JdbcMovieDao implements IMovieDao {

    private final Logger LOG = LoggerFactory.getLogger(getClass());

    @Autowired
    private JdbcTemplate jdbcTemplate;
    @Autowired
    private String getAllMoviesSQL;

    @Override
    public List<Movie> getAll() {
        LOG.info("Start query to get all movies from DB");
        long startTime = System.currentTimeMillis();
        List<Movie> movies = jdbcTemplate.query(getAllMoviesSQL, new MovieRowMapper());
        LOG.info("Finish query to get all movies from DB. It took {} ms", System.currentTimeMillis() - startTime);
        return movies;
    }
}
