package com.miskevich.movieland.service;

import com.miskevich.movieland.entity.Country;
import com.miskevich.movieland.entity.Movie;

import java.util.List;

public interface ICountryService {
    List<Country> getByMovieId(int movieId);

    Movie enrichWithCountry(Movie movie);
}
