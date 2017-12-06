package com.miskevich.movieland.dao.jdbc.mapper;

import com.miskevich.movieland.entity.Movie;
import com.miskevich.movieland.model.MovieRating;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class MovieRatingRowMapper implements RowMapper<MovieRating> {

    @Override
    public MovieRating mapRow(ResultSet resultSet, int rowNum) throws SQLException {
        MovieRating movieRating = new MovieRating();
        Movie movie = new Movie();
        movie.setId(resultSet.getInt("movie_id"));

        movieRating.setMovie(movie);
        movieRating.setRating(resultSet.getDouble("rating"));
        movieRating.setVotes(resultSet.getLong("votes"));

        return movieRating;
    }
}
