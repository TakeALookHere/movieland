package com.miskevich.movieland.service.impl;

import com.miskevich.movieland.dao.IGenreDao;
import com.miskevich.movieland.entity.Genre;
import com.miskevich.movieland.entity.Movie;
import com.miskevich.movieland.service.IGenreService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class GenreService implements IGenreService {

    @Autowired
    private IGenreDao genreDao;

    @Override
    public List<Genre> getAll() {
        return genreDao.getAll();
    }

    @Override
    public List<Genre> getByMovieId(int movieId) {
        return genreDao.getByMovieId(movieId);
    }

    @Override
    public void enrichWithGenre(Movie movie) {
        movie.setGenres(getByMovieId(movie.getId()));
    }

    @Override
    public void persist(Movie movie) {
        genreDao.persist(movie);
    }

    @Override
    public void remove(Movie movie) {
        genreDao.remove(movie);
    }

}