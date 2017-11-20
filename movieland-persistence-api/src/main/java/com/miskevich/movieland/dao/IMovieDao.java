package com.miskevich.movieland.dao;

import com.miskevich.movieland.entity.Movie;
import com.miskevich.movieland.model.SortingField;
import com.miskevich.movieland.model.SortingType;

import java.util.List;
import java.util.Map;

public interface IMovieDao {

    List<Movie> getAll(Map<SortingField, SortingType> params);

    List<Movie> getThreeRandomMovies();

    List<Movie> getByGenre(int id, Map<SortingField, SortingType> params);

    Movie getById(int id);

    Movie save(Movie movie);
}
