package com.miskevich.movieland.service.impl;

import com.miskevich.movieland.dao.IMovieDao;
import com.miskevich.movieland.entity.Movie;
import com.miskevich.movieland.model.SortingField;
import com.miskevich.movieland.model.SortingType;
import com.miskevich.movieland.service.ICountryService;
import com.miskevich.movieland.service.IGenreService;
import com.miskevich.movieland.service.IMovieService;
import com.miskevich.movieland.service.IReviewService;
import com.miskevich.movieland.service.cache.MovieCache;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
public class MovieService implements IMovieService {

    @Autowired
    private IMovieDao movieDao;
    @Autowired
    private IGenreService genreService;
    @Autowired
    private ICountryService countryService;
    @Autowired
    private IReviewService reviewService;
    @Autowired
    private MovieCache movieCache;

    @Override
    public List<Movie> getAll(Map<SortingField, SortingType> params) {
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
    public List<Movie> getByGenre(int id, Map<SortingField, SortingType> params) {
        return movieDao.getByGenre(id, params);
    }

    @Override
    public Movie getById(int id) {
        Optional<Movie> optional = movieCache.get(id);
        if(!optional.isPresent()){
            Movie movie = movieDao.getById(id);
            genreService.enrichWithGenre(movie);
            countryService.enrichWithCountry(movie);
            reviewService.enrichWithReview(movie);
            movieCache.put(id, movie);
            return movie;
        }
        return optional.get();
    }

    @Override
    //SQLIntegrityConstraintViolationException is not unchecked, but spring throw DuplicateKeyException (Runtime) for it. So how to control this?
    @Transactional
    public Movie save(Movie movie) {
        movie = movieDao.save(movie);
        genreService.persist(movie);
        countryService.persist(movie);
        reviewService.persist(movie);
        movieCache.put(movie.getId(), movie);
        return movie;
    }

    @Override
    @Transactional
    public Movie update(Movie movie) {
        movieDao.update(movie);
        genreService.update(movie);
        countryService.update(movie);
        reviewService.update(movie);
        movieCache.put(movie.getId(), movie);
        return movie;
    }
}
