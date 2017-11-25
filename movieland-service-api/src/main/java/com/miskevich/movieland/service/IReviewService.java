package com.miskevich.movieland.service;

import com.miskevich.movieland.entity.Movie;
import com.miskevich.movieland.entity.Review;

import java.util.List;

public interface IReviewService {

    List<Review> getByMovieId(int movieId);

    void enrichWithReview(Movie movie);

    Review add(Review review);

    void persist(Movie movie);

    void update(Movie movie);
}
