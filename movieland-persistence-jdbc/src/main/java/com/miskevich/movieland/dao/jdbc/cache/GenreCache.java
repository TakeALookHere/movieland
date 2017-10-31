package com.miskevich.movieland.dao.jdbc.cache;

import com.miskevich.movieland.dao.IGenreDao;
import com.miskevich.movieland.dao.jdbc.JdbcGenreDao;
import com.miskevich.movieland.entity.Genre;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.PropertySource;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Repository;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

@Repository
@PropertySource("classpath:database.properties")
@EnableScheduling
public class GenreCache implements IGenreDao {

    private final Logger LOG = LoggerFactory.getLogger(getClass());
    @Autowired
    private JdbcGenreDao genreDao;
    private List<Genre> genres;

    @PostConstruct
    private void init(){
        initCache();
    }

    @Override
    public List<Genre> getAll() {
        return genres;
    }

    private void initCache(){
        genres = genreDao.getAll();
        LOG.info("Genre cache was initialized");
    }

    @Scheduled(initialDelayString="${init.delay.genre.cache}", fixedRateString = "${fixed.rate.genre.cache}")
    private void initCacheRefreshTask(){
        initCache();
    }
}