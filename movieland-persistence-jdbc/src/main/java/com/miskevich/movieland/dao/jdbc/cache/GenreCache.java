package com.miskevich.movieland.dao.jdbc.cache;

import com.miskevich.movieland.dao.IGenreDao;
import com.miskevich.movieland.dao.jdbc.JdbcGenreDao;
import com.miskevich.movieland.entity.Genre;
import com.miskevich.movieland.entity.Movie;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Repository;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.List;

@Repository
@Primary
public class GenreCache implements IGenreDao {

    private final Logger LOG = LoggerFactory.getLogger(getClass());
    @Autowired
    private JdbcGenreDao genreDao;
    private volatile List<Genre> genres;

    @Override
    public List<Genre> getAll() {
        List<Genre> genreRef = genres;
        return new ArrayList<>(genreRef);
    }

    @Override
    public List<Genre> getByMovieId(int movieId) {
        return genreDao.getByMovieId(movieId);
    }

    @Override
    public void persist(Movie movie) {
        genreDao.persist(movie);
    }

    @Override
    public void update(Movie movie) {
        genreDao.update(movie);
    }

    @PostConstruct
    private void initCache() {
        genres = genreDao.getAll();
        LOG.info("Genre cache was initialized");
    }

    @Scheduled(initialDelayString = "${init.delay.genre.cache}", fixedRateString = "${fixed.rate.genre.cache}")
    private void initCacheRefreshTask() {
        initCache();
    }
}