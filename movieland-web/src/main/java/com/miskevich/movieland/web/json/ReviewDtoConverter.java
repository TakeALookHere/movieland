package com.miskevich.movieland.web.json;

import com.miskevich.movieland.entity.Movie;
import com.miskevich.movieland.entity.Review;
import com.miskevich.movieland.web.dto.ReviewDto;

public abstract class ReviewDtoConverter {

    public static Review mapDtoIntoObject(ReviewDto reviewDto) {
        Review review = new Review();
        Movie movie = new Movie();
        movie.setId(reviewDto.getMovieId());
        review.setMovie(movie);
        review.setText(reviewDto.getText());
        return review;
    }
}
