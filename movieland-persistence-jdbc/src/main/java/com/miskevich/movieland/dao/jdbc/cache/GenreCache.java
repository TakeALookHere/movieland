package com.miskevich.movieland.dao.jdbc.cache;

import com.miskevich.movieland.dao.IGenreDao;
import com.miskevich.movieland.dao.jdbc.JdbcGenreDao;
import com.miskevich.movieland.entity.Genre;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Repository;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

@Repository("genreCache")
@EnableScheduling
public class GenreCache implements IGenreDao {

    private final Logger LOG = LoggerFactory.getLogger(getClass());
    private static final ReadWriteLock READ_WRITE_LOCK = new ReentrantReadWriteLock();
    @Resource(name = "genreDao")
    private JdbcGenreDao genreDao;
    private List<Genre> genres;

    @Override
    public List<Genre> getAll() {
        List<Genre> copy = new ArrayList<>();
        READ_WRITE_LOCK.readLock().lock();

        for (Genre genre : genres) {
            copy.add(genre);
        }

        READ_WRITE_LOCK.readLock().unlock();
        return copy;
    }

    @PostConstruct
    private void initCache() {
        READ_WRITE_LOCK.writeLock().lock();
        genres = genreDao.getAll();
        READ_WRITE_LOCK.writeLock().unlock();
        LOG.info("Genre cache was initialized");
    }

    @Scheduled(initialDelayString = "${init.delay.genre.cache}", fixedRateString = "${fixed.rate.genre.cache}")
    private void initCacheRefreshTask() {
        initCache();
    }
}