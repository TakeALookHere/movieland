package com.miskevich.movieland.service;

import com.miskevich.movieland.entity.Movie;
import com.miskevich.movieland.model.SortPower;

import java.util.List;

public interface IMovieService {

    List<Movie> getAll(SortPower sortPower);
    List<Movie> getThreeRandomMovies();
    List<Movie> getByGenre(int id);
}
