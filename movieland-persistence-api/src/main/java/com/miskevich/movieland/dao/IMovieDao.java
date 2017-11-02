package com.miskevich.movieland.dao;

import com.miskevich.movieland.entity.Movie;

import java.util.List;

public interface IMovieDao {

    List<Movie> getAll();
    List<Movie> getAllRatingDesc();
    List<Movie> getAllPriceAsc();
    List<Movie> getAllPriceDesc();
    List<Movie> getThreeRandomMovies();
    List<Movie> getByGenre(int id);
}
