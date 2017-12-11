package com.miskevich.movieland.web.dto;

import com.miskevich.movieland.entity.Movie;
import com.miskevich.movieland.entity.User;

public class MovieRatingDto {

    private Movie movie;
    private User user;
    private double rating;

    public Movie getMovie() {
        return movie;
    }

    public void setMovie(Movie movie) {
        this.movie = movie;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public double getRating() {
        return rating;
    }

    public void setRating(double rating) {
        this.rating = rating;
    }

    @Override
    public String toString() {
        return "MovieRatingDto{" +
                "movie=" + movie +
                ", user=" + user +
                ", rating=" + rating +
                '}';
    }
}
