package com.miskevich.movieland.dao.jdbc;

import com.miskevich.movieland.dao.IMovieDao;
import com.miskevich.movieland.dao.jdbc.mapper.MovieRowMapper;
import com.miskevich.movieland.entity.Movie;
import com.miskevich.movieland.model.SortingField;
import com.miskevich.movieland.model.SortingType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.namedparam.EmptySqlParameterSource;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Date;
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
    private String getMoviesCountSQL;
    @Autowired
    private String getThreeRandomMoviesSQL;
    @Autowired
    private String getByGenreSQL;
    @Autowired
    private String getMovieByIdSQL;
    @Autowired
    private String addMovieSQL;
    @Autowired
    private String updateMovieSQL;

    @Override
    public List<Movie> getAll(Map<SortingField, SortingType> params) {
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
        long startTime = System.currentTimeMillis();
        LOG.info("Start query to get 3 random movies from DB");

        Set<Integer> movieIds = prepareRandomMovieIds();

        MapSqlParameterSource parameters = new MapSqlParameterSource();
        parameters.addValue("ids", movieIds);
        List<Movie> movies = namedParameterJdbcTemplate.query(getThreeRandomMoviesSQL, parameters, MOVIE_ROW_MAPPER);
        LOG.info("Finish query to get 3 random movies from DB. It took {} ms", System.currentTimeMillis() - startTime);
        return movies;
    }

    @Override
    public List<Movie> getByGenre(int id, Map<SortingField, SortingType> params) {
        List<Movie> movies;
        MapSqlParameterSource parameters = new MapSqlParameterSource("genreId", id);

        long startTime = System.currentTimeMillis();

        if (params.isEmpty()) {
            LOG.info("Start query to get movies by genre from DB");
            movies = namedParameterJdbcTemplate.query(getByGenreSQL, parameters, MOVIE_ROW_MAPPER);
            LOG.info("Finish query to get movies by genre from DB. It took {} ms", System.currentTimeMillis() - startTime);
        } else {
            String sortingSQL = generateSortingSQL(params);
            LOG.info("Start query to get movies by genre from DB with sorting");
            String allMoviesByGenreWithSortingSQL = getByGenreSQL + sortingSQL;
            LOG.info(allMoviesByGenreWithSortingSQL);
            movies = namedParameterJdbcTemplate.query(allMoviesByGenreWithSortingSQL, parameters, MOVIE_ROW_MAPPER);
            LOG.info("Finish query to get movies by genre from DB with sorting. It took {} ms", System.currentTimeMillis() - startTime);
        }

        return movies;
    }

    @Override
    public Movie getById(int id) {
        MapSqlParameterSource parameters = new MapSqlParameterSource("movieId", id);

        long startTime = System.currentTimeMillis();
        LOG.info("Start query to get movie by id from DB");
        Movie movie = namedParameterJdbcTemplate.queryForObject(getMovieByIdSQL, parameters, MOVIE_ROW_MAPPER);
        LOG.info("Finish query to get movie by id from DB. It took {} ms", System.currentTimeMillis() - startTime);
        return movie;
    }

    @Override
    public Movie save(Movie movie) {
        MapSqlParameterSource parameters = populateSQLParameters(movie);
        LOG.info("Start query to insert movie");
        long startTime = System.currentTimeMillis();
        KeyHolder keyHolder = new GeneratedKeyHolder();
        namedParameterJdbcTemplate.update(addMovieSQL, parameters, keyHolder);
        int movieId = keyHolder.getKey().intValue();
        movie.setId(movieId);
        LOG.info("Finish query to insert movie into DB. It took {} ms", System.currentTimeMillis() - startTime);
        return movie;
    }

    @Override
    public Movie update(Movie movie) {
        MapSqlParameterSource parameters = populateSQLParameters(movie);
        parameters.addValue("movieId", movie.getId());
        LOG.info("Start query to update movie");
        long startTime = System.currentTimeMillis();
        namedParameterJdbcTemplate.update(updateMovieSQL, parameters);
        LOG.info("Finish query to update movie into DB. It took {} ms", System.currentTimeMillis() - startTime);
        return movie;
    }

    private Set<Integer> prepareRandomMovieIds() {
        Integer moviesCount = namedParameterJdbcTemplate.queryForObject(getMoviesCountSQL, EmptySqlParameterSource.INSTANCE, Integer.class);
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

    String generateSortingSQL(Map<SortingField, SortingType> params) {
        int counter = 1;
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(" ORDER BY ");

        for (Map.Entry<SortingField, SortingType> param : params.entrySet()) {
            SortingField columnName = param.getKey();
            if (columnName.equals(SortingField.RATING)
                    || columnName.equals(SortingField.PRICE)) {
                stringBuilder.append("substring(")
                        .append(columnName)
                        .append(" from 1 for 9) ")
                        .append(param.getValue());
            } else {
                stringBuilder.append("IF(NAME_RUSSIAN RLIKE '^[a-z]', 1, 2), NAME_RUSSIAN ")
                        .append(param.getValue());
            }

            if (counter < params.size()) {
                stringBuilder.append(", ");
            }
            counter++;
        }
        return stringBuilder.toString();
    }

    private MapSqlParameterSource populateSQLParameters(Movie movie) {
        MapSqlParameterSource parameters = new MapSqlParameterSource();
        parameters.addValue("nameRussian", movie.getNameRussian());
        parameters.addValue("nameNative", movie.getNameNative());
        parameters.addValue("yearOfRelease", Date.valueOf(movie.getYearOfRelease()));
        parameters.addValue("description", movie.getDescription());
        parameters.addValue("rating", movie.getRating());
        parameters.addValue("price", movie.getPrice());
        parameters.addValue("picturePath", movie.getPicturePath());
        return parameters;
    }
}
