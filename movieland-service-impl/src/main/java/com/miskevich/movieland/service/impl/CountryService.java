package com.miskevich.movieland.service.impl;

import com.miskevich.movieland.dao.ICountryDao;
import com.miskevich.movieland.entity.Country;
import com.miskevich.movieland.entity.Movie;
import com.miskevich.movieland.service.ICountryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CountryService implements ICountryService {

    @Autowired
    private ICountryDao countryDao;

    @Override
    public List<Country> getByMovieId(int movieId) {
        return countryDao.getByMovieId(movieId);
    }

    @Override
    public List<Country> getAll() {
        return countryDao.getAll();
    }

    @Override
    public void persist(Movie movie) {
        countryDao.persist(movie);
    }

    @Override
    public void remove(Movie movie) {
        countryDao.remove(movie);
    }
}
