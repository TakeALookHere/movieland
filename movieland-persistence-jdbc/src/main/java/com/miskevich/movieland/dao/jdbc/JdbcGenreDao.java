package com.miskevich.movieland.dao.jdbc;

import com.miskevich.movieland.dao.IGenreDao;
import com.miskevich.movieland.dao.jdbc.mapper.GenreRowMapper;
import com.miskevich.movieland.entity.Genre;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class JdbcGenreDao implements IGenreDao {
    private final Logger LOG = LoggerFactory.getLogger(getClass());
    private final static GenreRowMapper GENRE_ROW_MAPPER = new GenreRowMapper();
    @Autowired
    private JdbcTemplate jdbcTemplate;
    @Autowired
    private String getAllGenresSQL;

    @Override
    @Cacheable(value = "genreCache")
    public List<Genre> getAll() {
        LOG.info("Start query to get all genres from DB");
        long startTime = System.currentTimeMillis();
        List<Genre> genres = jdbcTemplate.query(getAllGenresSQL, GENRE_ROW_MAPPER);
        LOG.info("Finish query to get all genres from DB. It took {} ms", System.currentTimeMillis() - startTime);
        return genres;
    }
}