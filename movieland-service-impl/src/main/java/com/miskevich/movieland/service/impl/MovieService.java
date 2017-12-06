package com.miskevich.movieland.service.impl;

import com.miskevich.movieland.dao.IMovieDao;
import com.miskevich.movieland.entity.Movie;
import com.miskevich.movieland.model.MovieRating;
import com.miskevich.movieland.model.SortingField;
import com.miskevich.movieland.model.SortingType;
import com.miskevich.movieland.service.ICountryService;
import com.miskevich.movieland.service.IGenreService;
import com.miskevich.movieland.service.IMovieService;
import com.miskevich.movieland.service.IReviewService;
import com.miskevich.movieland.service.cache.MovieCache;
import com.miskevich.movieland.service.model.EnrichmentType;
import com.miskevich.movieland.service.util.MovieParallelEnricher;
import com.miskevich.movieland.service.util.MovieRatingBuffer;
import com.miskevich.movieland.service.util.MovieRatingCurrent;
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
    @Autowired
    private MovieParallelEnricher movieParallelEnricher;
    @Autowired
    private MovieRatingBuffer movieRatingBuffer;
    @Autowired
    private MovieRatingCurrent movieRatingCurrent;

    @Override
    public List<Movie> getAll(Map<SortingField, SortingType> params) {
        return movieDao.getAll(params);
    }

    @Override
    public List<Movie> getThreeRandomMovies() {
        List<Movie> movies = movieDao.getThreeRandomMovies();
        for (Movie movie : movies) {
            movieParallelEnricher.enrich(movie, EnrichmentType.PARTIAL);
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
        if (!optional.isPresent()) {
            Movie movie = movieDao.getById(id);
            movieParallelEnricher.enrich(movie, EnrichmentType.FULL);
            movieCache.add(movie);
            return movie;
        }
        return optional.get();
    }

    @Override
    @Transactional
    public Movie persist(Movie movie) {
        movie = movieDao.persist(movie);
        genreService.persist(movie);
        countryService.persist(movie);
        reviewService.persist(movie);
        movieCache.add(movie);
        return movie;
    }

    @Override
    @Transactional
    public Movie update(Movie movie) {
        movieDao.update(movie);
        genreService.remove(movie);
        genreService.persist(movie);
        countryService.remove(movie);
        countryService.persist(movie);
        reviewService.update(movie);
        movieCache.add(movie);
        return movie;
    }

    @Override
    public Movie rate(MovieRating movieRating) {
        movieRatingBuffer.add(movieRating);
        return movieRatingCurrent.refreshRating(movieRating);
    }
}
