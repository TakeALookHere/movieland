package com.miskevich.movieland.service.impl;

import com.miskevich.movieland.dao.IMovieDao;
import com.miskevich.movieland.entity.Movie;
import com.miskevich.movieland.service.IMovieService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MovieService implements IMovieService {

    @Autowired
    private IMovieDao movieDao;

    @Override
    public List<Movie> getAll() {
        return movieDao.getAll();
    }

    @Override
    public List<Movie> getThreeRandomMovies() {
        return movieDao.getThreeRandomMovies();
    }
}
