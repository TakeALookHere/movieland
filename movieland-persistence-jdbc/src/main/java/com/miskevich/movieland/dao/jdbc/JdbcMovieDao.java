package com.miskevich.movieland.dao.jdbc;

import com.miskevich.movieland.dao.IMovieDao;
import com.miskevich.movieland.dao.jdbc.mapper.MovieRowMapper;
import com.miskevich.movieland.entity.Movie;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.*;

@Repository
public class JdbcMovieDao implements IMovieDao {

    private final Logger LOG = LoggerFactory.getLogger(getClass());
    private static final MovieRowMapper MOVIE_ROW_MAPPER = new MovieRowMapper();
    @Autowired
    private JdbcTemplate jdbcTemplate;
    @Autowired
    private NamedParameterJdbcTemplate namedParameterJdbcTemplate;
    @Autowired
    private String getAllMoviesSQL;
    @Autowired
    private String getMoviesCount;
    @Autowired
    private String getThreeRandomMovies;
    @Autowired
    private String getByGenre;

    @Override
    public List<Movie> getAll() {
        LOG.info("Start query to get all movies from DB");
        long startTime = System.currentTimeMillis();
        List<Movie> movies = jdbcTemplate.query(getAllMoviesSQL, MOVIE_ROW_MAPPER);
        LOG.info("Finish query to get all movies from DB. It took {} ms", System.currentTimeMillis() - startTime);
        return movies;
    }

    @Override
    public List<Movie> getThreeRandomMovies() {
        LOG.info("Start query to get 3 random movies from DB");
        long startTime = System.currentTimeMillis();

        Set<Integer> movieIds = prepareRandomMovieIds();

        MapSqlParameterSource parameters = new MapSqlParameterSource();
        parameters.addValue("ids", movieIds);
        List<Movie> movies = namedParameterJdbcTemplate.query(getThreeRandomMovies, parameters, MOVIE_ROW_MAPPER);
        LOG.info("Finish query to get 3 random movies from DB. It took {} ms", System.currentTimeMillis() - startTime);
        return movies;
    }

    @Override
    public List<Movie> getByGenre(int id) {
        LOG.info("Start query to get movies by genre from DB");
        long startTime = System.currentTimeMillis();

        MapSqlParameterSource parameters = new MapSqlParameterSource();
        parameters.addValue("genreId", id);
        List<Movie> movies = namedParameterJdbcTemplate.query(getByGenre, parameters, MOVIE_ROW_MAPPER);
        LOG.info("Finish query to get movies by genre from DB. It took {} ms", System.currentTimeMillis() - startTime);
        return movies;
    }

    private Set<Integer> prepareRandomMovieIds() {
        Integer moviesCount = jdbcTemplate.queryForObject(getMoviesCount, Integer.class);
        return generateRandomMovieIds(moviesCount);
    }

    private int getRandomMovie(int moviesCount) {
        Random random = new Random();
        return random.nextInt(moviesCount) + 1;
    }

    private Set<Integer> generateRandomMovieIds(Integer moviesCount) {
        Set<Integer> randomMovies = new HashSet<>();
        for (int i = 0; i < 3; i++) {
            randomMovies.add(getRandomMovie(moviesCount));
        }
        return randomMovies;
    }
}
