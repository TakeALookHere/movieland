package com.miskevich.movieland.dao.jdbc.cache;

import com.miskevich.movieland.dao.IGenreDao;
import com.miskevich.movieland.dao.jdbc.JdbcGenreDao;
import com.miskevich.movieland.entity.Genre;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Repository;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

@Repository
@EnableScheduling
public class GenreCache implements IGenreDao {

    private final Logger LOG = LoggerFactory.getLogger(getClass());
    private static final ReadWriteLock READ_WRITE_LOCK = new ReentrantReadWriteLock();
    @Autowired
    private JdbcGenreDao genreDao;
    private List<Genre> genres;

    @Override
    public List<Genre> getAll() {
        List<Genre> copy = new ArrayList<>();
        Lock lock = READ_WRITE_LOCK.readLock();
        try {
            lock.lock();
            copy.addAll(genres);
        } finally {

            lock.unlock();
        }
        return copy;
    }

    @Override
    public List<Genre> getByMovieId(int movieId) {
        return genreDao.getByMovieId(movieId);
    }

    @PostConstruct
    private void initCache() {
        Lock lock = READ_WRITE_LOCK.writeLock();
        try {
            lock.lock();
            genres = genreDao.getAll();
        } finally {
            lock.unlock();
        }
        LOG.info("Genre cache was initialized");
    }

    @Scheduled(initialDelayString = "${init.delay.genre.cache}", fixedRateString = "${fixed.rate.genre.cache}")
    private void initCacheRefreshTask() {
        initCache();
    }
}