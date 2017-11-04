package com.miskevich.movieland.web.json;

import com.miskevich.movieland.entity.Movie;
import com.miskevich.movieland.web.dto.MovieDto;

import java.util.ArrayList;
import java.util.List;

public abstract class DtoConverter {

    public static List<MovieDto> mapList(List<Movie> fromList) {
        List<MovieDto> movieDtos = new ArrayList<>();
        for (Movie movie : fromList) {
            movieDtos.add(mapObject(movie));
        }
        return movieDtos;
    }

    private static MovieDto mapObject(Movie movie) {
        MovieDto movieDto = new MovieDto();
        movieDto.setId(movie.getId());
        movieDto.setNameRussian(movie.getNameRussian());
        movieDto.setNameNative(movie.getNameNative());
        movieDto.setYearOfRelease(movie.getYearOfRelease());
        movieDto.setCountries(movie.getCountries());
        movieDto.setDescription(movie.getDescription());
        movieDto.setRating(movie.getRating());
        movieDto.setPrice(movie.getPrice());
        movieDto.setPicturePath(movie.getPicturePath());
        movieDto.setGenres(movie.getGenres());
        movieDto.setReviews(movie.getReviews());
        return movieDto;
    }
}
