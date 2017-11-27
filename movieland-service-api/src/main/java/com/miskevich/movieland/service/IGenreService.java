package com.miskevich.movieland.service;

import com.miskevich.movieland.entity.Genre;
import com.miskevich.movieland.entity.Movie;

import java.util.List;

public interface IGenreService {
    List<Genre> getAll();

    List<Genre> getByMovieId(int movieId);

    void enrichWithGenre(Movie movie);

    void persist(Movie movie);

    void remove(Movie movie);
}
