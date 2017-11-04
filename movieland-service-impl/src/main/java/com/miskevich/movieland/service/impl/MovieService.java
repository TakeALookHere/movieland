package com.miskevich.movieland.service.impl;

import com.miskevich.movieland.dao.IMovieDao;
import com.miskevich.movieland.entity.Movie;
import com.miskevich.movieland.service.ICountryService;
import com.miskevich.movieland.service.IGenreService;
import com.miskevich.movieland.service.IMovieService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class MovieService implements IMovieService {

    @Autowired
    private IMovieDao movieDao;
    @Autowired
    private IGenreService genreService;
    @Autowired
    private ICountryService countryService;

    @Override
    public List<Movie> getAll(Map<String, String> params) {
        return movieDao.getAll(params);
    }

    @Override
    public List<Movie> getThreeRandomMovies() {
        List<Movie> movies = movieDao.getThreeRandomMovies();
        for (Movie movie : movies) {
            genreService.enrichWithGenre(movie);
            countryService.enrichWithCountry(movie);
        }
        return movies;
    }

    @Override
    public List<Movie> getByGenre(int id, Map<String, String> params) {
        return movieDao.getByGenre(id, params);
    }
}
