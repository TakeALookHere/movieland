package com.miskevich.movieland.model;

import com.miskevich.movieland.entity.Movie;
import com.miskevich.movieland.entity.User;

public class MovieRating {

    private Movie movie;
    private User user;
    private double rating;
    private long votes;

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

    public long getVotes() {
        return votes;
    }

    public void setVotes(long votes) {
        this.votes = votes;
    }

    @Override
    public String toString() {
        return "MovieRating{" +
                "movie=" + movie +
                ", user=" + user +
                ", rating=" + rating +
                ", votes=" + votes +
                '}';
    }
}
