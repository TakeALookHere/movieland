package com.miskevich.movieland.dao;

import com.miskevich.movieland.entity.Movie;

import java.util.List;
import java.util.Map;

public interface IMovieDao {

    List<Movie> getAll(Map<String, String> params);

    List<Movie> getThreeRandomMovies();

    List<Movie> getByGenre(int id, Map<String, String> params);

    Movie getById(int id);
}
