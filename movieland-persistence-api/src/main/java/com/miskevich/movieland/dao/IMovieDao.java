package com.miskevich.movieland.dao;

import com.miskevich.movieland.entity.Movie;

import java.util.List;

public interface IMovieDao {

    List<Movie> getAll();
    List<Movie> getThreeRandomMovies();
    List<Movie> getByGenre(int id);
}
