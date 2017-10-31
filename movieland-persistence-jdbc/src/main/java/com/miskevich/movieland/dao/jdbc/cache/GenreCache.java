package com.miskevich.movieland.dao.jdbc.cache;

import com.miskevich.movieland.dao.IGenreDao;
import com.miskevich.movieland.dao.jdbc.JdbcGenreDao;
import com.miskevich.movieland.entity.Genre;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

@Repository
public class GenreCache implements IGenreDao {

    private final Logger LOG = LoggerFactory.getLogger(getClass());
    @Autowired
    private JdbcGenreDao genreDao;
    private List<Genre> genres;
    private ScheduledExecutorService executorService = Executors.newScheduledThreadPool(1);

    @Override
    public List<Genre> getAll() {
        List<Genre> copy = new ArrayList<>();
        if (genres == null) {
            genres = genreDao.getAll();
            copy = cloneGenres(genres, copy);
            initCacheRefreshTask();
        } else {
            long startTime = System.currentTimeMillis();
            copy = cloneGenres(genres, copy);
            LOG.info("Finish query to get all genres from cache. It took {} ms", System.currentTimeMillis() - startTime);
        }
        return copy;
    }

    private List<Genre> cloneGenres(List<Genre> genres, List<Genre> copy) {
        for (Genre genre : genres) {
            copy.add(genre);
        }
        return copy;
    }

    private void initCacheRefreshTask() {
        executorService.schedule((Callable) () -> {
                    clearCache();
                    return "Clear cache init was called...";
                },
                40,
                TimeUnit.SECONDS);
    }

    private void clearCache() {
        genres = null;
        LOG.info("Genre cache was cleared");
    }
}