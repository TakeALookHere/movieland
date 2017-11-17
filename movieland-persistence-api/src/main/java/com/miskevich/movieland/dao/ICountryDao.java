package com.miskevich.movieland.dao;

import com.miskevich.movieland.entity.Country;

import java.util.List;

public interface ICountryDao {
    List<Country> getByMovieId(int movieId);
    List<Country> getAll();
}
