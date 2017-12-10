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

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

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
    @Autowired
    private String removeGenresSQL;

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
    public void persist(Movie movie) {
        List<Genre> genres = movie.getGenres();
        if (genres != null) {
            int movieId = movie.getId();

            List<Map<String, Object>> batchValues = new ArrayList<>(genres.size());
            for (Genre genre : genres) {
                batchValues.add(
                        new MapSqlParameterSource("movieId", movieId)
                                .addValue("genreId", genre.getId())
                                .getValues());
            }

            LOG.info("Start query to insert genres for movieId {}", movieId);
            long startTime = System.currentTimeMillis();
            //How to deal with this unchecked assignment?
            namedParameterJdbcTemplate.batchUpdate(addMovieGenresSQL, batchValues.toArray(new Map[genres.size()]));
            LOG.info("Finish query to insert genres for movieId {}. It took {} ms", movieId, System.currentTimeMillis() - startTime);
        }
    }

    @Override
    public void remove(Movie movie) {
        if (movie.getGenres() != null) {
            int movieId = movie.getId();
            MapSqlParameterSource parameters = new MapSqlParameterSource("movieId", movieId);
            LOG.info("Start query to remove genres for movieId {}", movieId);
            long startTime = System.currentTimeMillis();
            namedParameterJdbcTemplate.update(removeGenresSQL, parameters);
            LOG.info("Finish query to remove genres for movieId {}. It took {} ms", movieId, System.currentTimeMillis() - startTime);
        }
    }
}