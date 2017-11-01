package com.miskevich.movieland.service.impl;

import com.miskevich.movieland.dao.IGenreDao;
import com.miskevich.movieland.entity.Genre;
import com.miskevich.movieland.service.IGenreService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

@Service
public class GenreService implements IGenreService {

    @Resource(name = "genreCache")
    private IGenreDao genreDao;

    @Override
    public List<Genre> getAll() {
        return genreDao.getAll();
    }
}