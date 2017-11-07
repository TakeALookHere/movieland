package com.miskevich.movieland.dao;

import com.miskevich.movieland.entity.Review;

import java.util.List;

public interface IReviewDao {
    List<Review> getByMovieId(int movieId);

    Review add(Review review);
}
