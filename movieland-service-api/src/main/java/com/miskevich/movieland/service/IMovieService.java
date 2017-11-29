package com.miskevich.movieland.service;

import com.miskevich.movieland.entity.Movie;
import com.miskevich.movieland.model.SortingField;
import com.miskevich.movieland.model.SortingType;

import java.util.List;
import java.util.Map;

public interface IMovieService {

    List<Movie> getAll(Map<SortingField, SortingType> params);

    List<Movie> getThreeRandomMovies();

    List<Movie> getByGenre(int id, Map<SortingField, SortingType> params);

    Movie getById(int id);

    Movie persist(Movie movie);

    Movie update(Movie movie);
}
