package com.miskevich.movieland.dao;

import com.miskevich.movieland.entity.Genre;
import com.miskevich.movieland.entity.Movie;

import java.util.List;

public interface IGenreDao {
    List<Genre> getAll();

    List<Genre> getByMovieId(int movieId);

    void persist(Movie movie);

    void update(Movie movie);
}