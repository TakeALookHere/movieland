package com.miskevich.movieland.dao.jdbc;

import com.miskevich.movieland.dao.IGenreDao;
import com.miskevich.movieland.dao.jdbc.mapper.GenreRowMapper;
import com.miskevich.movieland.entity.Genre;
import com.miskevich.movieland.entity.Movie;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class JdbcGenreDao implements IGenreDao {

    private final Logger LOG = LoggerFactory.getLogger(getClass());
    private final static GenreRowMapper GENRE_ROW_MAPPER = new GenreRowMapper();
    @Autowired
    private NamedParameterJdbcTemplate namedParameterJdbcTemplate;
    @Autowired
    private String getAllGenresSQL;
    @Autowired
    private String getGenresByMovieIdSQL;
    @Autowired
    private String addMovieGenresSQL;

    @Override
    public List<Genre> getAll() {
        LOG.info("Start query to get all genres from DB");
        long startTime = System.currentTimeMillis();
        List<Genre> genres = namedParameterJdbcTemplate.query(getAllGenresSQL, GENRE_ROW_MAPPER);
        LOG.info("Finish query to get all genres from DB. It took {} ms", System.currentTimeMillis() - startTime);
        return genres;
    }

    @Override
    public List<Genre> getByMovieId(int movieId) {
        MapSqlParameterSource parameters = new MapSqlParameterSource();
        parameters.addValue("movieId", movieId);

        LOG.info("Start query to get genres from DB by movieId");
        long startTime = System.currentTimeMillis();
        List<Genre> genres = namedParameterJdbcTemplate.query(getGenresByMovieIdSQL, parameters, GENRE_ROW_MAPPER);
        LOG.info("Finish query to get genres from DB by movieId. It took {} ms", System.currentTimeMillis() - startTime);
        return genres;
    }

    @Override
    public void saveMovieGenres(Movie movie) {
        for (int i = 0; i < movie.getGenres().size(); i++) {
            MapSqlParameterSource parameters = new MapSqlParameterSource();
            int movieId = movie.getId();
            int genreId = movie.getGenres().get(i).getId();
            parameters.addValue("movieId", movieId);
            parameters.addValue("genreId", genreId);

            LOG.info("Start query to insert genreId {} for movieId {}", genreId, movieId);
            long startTime = System.currentTimeMillis();
            namedParameterJdbcTemplate.update(addMovieGenresSQL, parameters);
            LOG.info("Finish query to insert genreId {} for movieId {}. It took {} ms", genreId, movieId, System.currentTimeMillis() - startTime);
        }
    }
}