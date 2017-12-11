package com.miskevich.movieland.web.json;

import com.miskevich.movieland.model.MovieRating;
import com.miskevich.movieland.web.dto.MovieRatingDto;

public abstract class MovieRatingDtoConverter {
    public static MovieRating mapDtoIntoObject(MovieRatingDto movieRatingDto){
        MovieRating movieRating = new MovieRating();
        movieRating.setRating(movieRatingDto.getRating());
        movieRating.setMovie(movieRatingDto.getMovie());
        movieRating.setUser(movieRatingDto.getUser());
        return movieRating;
    }
}
