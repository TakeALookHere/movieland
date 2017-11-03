package com.miskevich.movieland.dao.jdbc;

import com.miskevich.movieland.dao.IMovieDao;
import com.miskevich.movieland.dao.jdbc.mapper.MovieRowMapper;
import com.miskevich.movieland.entity.Movie;
import com.miskevich.movieland.model.SortingField;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.namedparam.EmptySqlParameterSource;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.*;

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

    @Override
    public List<Movie> getAll(Map<String, String> params) {
        List<Movie> movies;
        long startTime = System.currentTimeMillis();

        if (params.isEmpty()) {
            LOG.info("Start query to get all movies from DB");
            movies = namedParameterJdbcTemplate.query(getAllMoviesSQL, MOVIE_ROW_MAPPER);
            LOG.info("Finish query to get all movies from DB. It took {} ms", System.currentTimeMillis() - startTime);
        } else {
            String sortingSQL = generateSortingSQL(params);
            LOG.info("Start query to get all movies from DB with sorting");
            String allMoviesWithSortingSQL = getAllMoviesSQL + sortingSQL;
            LOG.info(allMoviesWithSortingSQL);
            movies = namedParameterJdbcTemplate.query(allMoviesWithSortingSQL, MOVIE_ROW_MAPPER);
            LOG.info("Finish query to get all movies from DB with sorting. It took {} ms", System.currentTimeMillis() - startTime);
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
    public List<Movie> getByGenre(int id, Map<String, String> params) {
        List<Movie> movies;
        MapSqlParameterSource parameters = new MapSqlParameterSource();
        parameters.addValue("genreId", id);
        long startTime = System.currentTimeMillis();

        if (params.isEmpty()) {
            LOG.info("Start query to get movies by genre from DB");
            movies = namedParameterJdbcTemplate.query(getByGenre, parameters, MOVIE_ROW_MAPPER);
            LOG.info("Finish query to get movies by genre from DB. It took {} ms", System.currentTimeMillis() - startTime);
        } else {
            String sortingSQL = generateSortingSQL(params);
            LOG.info("Start query to get movies by genre from DB with sorting");
            String allMoviesByGenreWithSortingSQL = getByGenre + sortingSQL;
            LOG.info(allMoviesByGenreWithSortingSQL);
            movies = namedParameterJdbcTemplate.query(allMoviesByGenreWithSortingSQL, parameters, MOVIE_ROW_MAPPER);
            LOG.info("Finish query to get movies by genre from DB with sorting. It took {} ms", System.currentTimeMillis() - startTime);
        }

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

    String generateSortingSQL(Map<String, String> params) {
        int counter = 1;
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(" ORDER BY ");

        for (Map.Entry<String, String> param : params.entrySet()) {
            String columnName = param.getKey();
            if (columnName.equalsIgnoreCase(SortingField.RATING.getValue())
                    || columnName.equalsIgnoreCase(SortingField.PRICE.getValue())) {
                stringBuilder.append("substring(")
                        .append(columnName)
                        .append(" from 1 for 9) ")
                        .append(param.getValue());
            } else {
                stringBuilder.append("IF(name_russian RLIKE '^[a-z]', 1, 2), name_russian ")
                        .append(param.getValue());
            }

            if (counter < params.size()) {
                stringBuilder.append(", ");
            }
            counter++;
        }
        return stringBuilder.toString();
    }
}
