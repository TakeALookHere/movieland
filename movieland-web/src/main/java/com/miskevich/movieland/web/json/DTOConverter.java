package com.miskevich.movieland.web.json;

import com.miskevich.movieland.entity.Movie;
import com.miskevich.movieland.web.dto.MovieDTO;

import java.util.ArrayList;
import java.util.List;

public abstract class DTOConverter {

    public static List<MovieDTO> mapList(List<Movie> fromList) {
        List<MovieDTO> movieDTOs = new ArrayList<>();
        for (Movie movie : fromList){
            movieDTOs.add(mapObject(movie));
        }
        return movieDTOs;
    }

    private static MovieDTO mapObject(Movie movie){
        MovieDTO movieDTO = new MovieDTO();
        movieDTO.setId(movie.getId());
        movieDTO.setNameRussian(movie.getNameRussian());
        movieDTO.setNameNative(movie.getNameNative());
        movieDTO.setReleasedDate(movie.getReleasedDate());
        movieDTO.setCountries(movie.getCountries());
        movieDTO.setPlot(movie.getPlot());
        movieDTO.setRating(movie.getRating());
        movieDTO.setPrice(movie.getPrice());
        movieDTO.setPicturePath(movie.getPicturePath());
        movieDTO.setGenres(movie.getGenres());
        movieDTO.setReviews(movie.getReviews());
        return movieDTO;
    }
}
