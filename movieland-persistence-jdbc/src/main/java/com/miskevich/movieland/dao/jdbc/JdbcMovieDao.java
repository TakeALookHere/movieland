package com.miskevich.movieland.dao.jdbc;

import com.miskevich.movieland.dao.IMovieDao;
import com.miskevich.movieland.dao.jdbc.mapper.MovieRowMapper;
import com.miskevich.movieland.entity.Movie;
import com.miskevich.movieland.model.SortPower;
import com.miskevich.movieland.model.SortingType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.namedparam.EmptySqlParameterSource;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.HashSet;
import java.util.List;
import java.util.Random;
import java.util.Set;

@Repository
public class JdbcMovieDao implements IMovieDao {

    private final Logger LOG = LoggerFactory.getLogger(getClass());
    private static final MovieRowMapper MOVIE_ROW_MAPPER = new MovieRowMapper();
    private static final Random RANDOM = new Random();

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
    @Autowired
    private String getAllRatingDescSQL;
    @Autowired
    private String getAllPriceAscSQL;
    @Autowired
    private String getAllPriceDescSQL;

    @Override
    public List<Movie> getAll(SortPower sortPower) {

        long startTime = System.currentTimeMillis();
        List<Movie> movies;
        SortingType ratingSort = sortPower.getRatingSort();
        SortingType priceSort = sortPower.getPriceSort();

        if (ratingSort != null && ratingSort.equals(SortingType.DESC)) {
            LOG.info("Start query to get all movies with rating desc from DB");
            movies = namedParameterJdbcTemplate.query(getAllRatingDescSQL, MOVIE_ROW_MAPPER);
            LOG.info("Finish query to get all movies with rating desc from DB. It took {} ms", System.currentTimeMillis() - startTime);
        } else if (priceSort != null && priceSort.equals(SortingType.ASC)) {
            LOG.info("Start query to get all movies with price asc from DB");
            movies = namedParameterJdbcTemplate.query(getAllPriceAscSQL, MOVIE_ROW_MAPPER);
            LOG.info("Finish query to get all movies with price asc from DB. It took {} ms", System.currentTimeMillis() - startTime);
        } else if (priceSort != null && priceSort.equals(SortingType.DESC)) {
            LOG.info("Start query to get all movies with price desc from DB");
            movies = namedParameterJdbcTemplate.query(getAllPriceDescSQL, MOVIE_ROW_MAPPER);
            LOG.info("Finish query to get all movies with price desc from DB. It took {} ms", System.currentTimeMillis() - startTime);
        } else {
            LOG.info("Start query to get all movies from DB");
            movies = namedParameterJdbcTemplate.query(getAllMoviesSQL, MOVIE_ROW_MAPPER);
            LOG.info("Finish query to get all movies from DB. It took {} ms", System.currentTimeMillis() - startTime);
        }

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
        Integer moviesCount = namedParameterJdbcTemplate.queryForObject(getMoviesCount, EmptySqlParameterSource.INSTANCE, Integer.class);
        return generateRandomMovieIds(moviesCount);
    }

    private int getRandomMovie(int moviesCount) {
        return RANDOM.nextInt(moviesCount) + 1;
    }

    private Set<Integer> generateRandomMovieIds(Integer moviesCount) {
        Set<Integer> randomMovies = new HashSet<>();
        while (randomMovies.size() < 3) {
            randomMovies.add(getRandomMovie(moviesCount));
        }
        return randomMovies;
    }
}
