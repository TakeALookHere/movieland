package com.miskevich.movieland.web.json;

import com.miskevich.movieland.entity.Movie;
import com.miskevich.movieland.web.dto.MovieDto;

import java.util.ArrayList;
import java.util.List;

public abstract class MovieDtoConverter {

    public static List<MovieDto> mapList(List<Movie> fromList) {
        List<MovieDto> movieDtos = new ArrayList<>();
        for (Movie movie : fromList) {
            movieDtos.add(mapObject(movie));
        }
        return movieDtos;
    }

    public static MovieDto mapObject(Movie movie) {
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

    public static Movie mapDtoIntoObject(MovieDto movieDto) {
        Movie movie = new Movie();
        movie.setNameRussian(movieDto.getNameRussian());
        movie.setNameNative(movieDto.getNameNative());
        movie.setYearOfRelease(movieDto.getYearOfRelease());
        movie.setDescription(movieDto.getDescription());
        movie.setRating(movieDto.getRating());
        movie.setPrice(movieDto.getPrice());
        movie.setPicturePath(movieDto.getPicturePath());
        movie.setGenres(movieDto.getGenres());
        movie.setCountries(movieDto.getCountries());
        return movie;
    }
}
