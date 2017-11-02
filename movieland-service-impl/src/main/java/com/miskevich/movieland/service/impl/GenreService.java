package com.miskevich.movieland.service.impl;

import com.miskevich.movieland.dao.IGenreDao;
import com.miskevich.movieland.entity.Genre;
import com.miskevich.movieland.service.IGenreService;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

public class GenreService implements IGenreService {

    @Autowired
    private IGenreDao genreCache;

    @Override
    public List<Genre> getAll() {
        return genreCache.getAll();
    }

    public void setGenreDao(IGenreDao genreDao) {
        this.genreCache = genreDao;
    }
}