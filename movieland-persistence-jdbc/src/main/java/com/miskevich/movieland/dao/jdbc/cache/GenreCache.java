package com.miskevich.movieland.dao.jdbc.cache;

import com.miskevich.movieland.dao.IGenreDao;
import com.miskevich.movieland.dao.jdbc.JdbcGenreDao;
import com.miskevich.movieland.entity.Genre;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import javax.annotation.PostConstruct;
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

    @PostConstruct
    private void init(){
        initCache();
        initCacheRefreshTask();
    }

    @Override
    public List<Genre> getAll() {
        return genres;
    }

//    private List<Genre> copy(List<Genre> genres){
//        List<Genre> copy = new ArrayList<>();
//        for (Genre genre : genres){
//            try {
//                copy.add((Genre) genre.clone());
//            } catch (CloneNotSupportedException e) {
//                LOG.error("ERROR", e);
//                throw new RuntimeException(e);
//            }
//        }
//        return copy;
//    }

    private void initCache(){
        genres = genreDao.getAll();
    }

    private void initCacheRefreshTask() {
        executorService.scheduleAtFixedRate(() -> {
                    clearCache();
                },
                40,
                40,
                TimeUnit.SECONDS);
    }

    private void clearCache() {
        genres = null;
        LOG.info("Genre cache was cleared");
    }
}