package com.miskevich.movieland.service;

import com.miskevich.movieland.entity.Movie;

import java.util.List;

public interface IMovieService {

    List<Movie> getAll();
    List<Movie> getAllRatingDesc();
    List<Movie> getAllPriceAsc();
    List<Movie> getAllPriceDesc();
    List<Movie> getThreeRandomMovies();
    List<Movie> getByGenre(int id);
}
