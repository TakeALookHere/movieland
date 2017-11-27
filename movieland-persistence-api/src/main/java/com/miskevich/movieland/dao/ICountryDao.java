package com.miskevich.movieland.dao;

import com.miskevich.movieland.entity.Country;
import com.miskevich.movieland.entity.Movie;

import java.util.List;

public interface ICountryDao {

    List<Country> getByMovieId(int movieId);

    List<Country> getAll();

    void persist(Movie movie);

    void remove(Movie movie);
}
