package com.miskevich.movieland.dao.jdbc.mapper;

import com.miskevich.movieland.entity.Movie;
import com.miskevich.movieland.entity.Review;
import com.miskevich.movieland.entity.User;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class ReviewRowMapper implements RowMapper<Review> {

    @Override
    public Review mapRow(ResultSet resultSet, int rowNum) throws SQLException {
        Review review = new Review();
        review.setId(resultSet.getLong("id"));
        review.setText(resultSet.getString("description"));

        Movie movie = new Movie();
        movie.setId(resultSet.getInt("movie_id"));
        review.setMovie(movie);

        User user = new User();
        user.setId(resultSet.getInt("user_id"));
        review.setUser(user);
        return review;
    }
}
