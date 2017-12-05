package com.miskevich.movieland.service.impl;

import com.miskevich.movieland.dao.IReviewDao;
import com.miskevich.movieland.entity.Movie;
import com.miskevich.movieland.entity.Review;
import com.miskevich.movieland.service.IReviewService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ReviewService implements IReviewService {

    @Autowired
    private IReviewDao reviewDao;

    @Override
    public List<Review> getByMovieId(int movieId) {
        return reviewDao.getByMovieId(movieId);
    }

    @Override
    public Review add(Review review) {
        return reviewDao.add(review);
    }

    @Override
    public void persist(Movie movie) {
        reviewDao.persist(movie);
    }

    @Override
    public void update(Movie movie) {
        reviewDao.update(movie);
    }
}
