package com.miskevich.movieland.service;

import com.miskevich.movieland.entity.Movie;
import com.miskevich.movieland.entity.Review;

import java.util.List;

public interface IReviewService {
    List<Review> getByMovieId(int movieId);

    Movie enrichWithReview(Movie movie);
}
