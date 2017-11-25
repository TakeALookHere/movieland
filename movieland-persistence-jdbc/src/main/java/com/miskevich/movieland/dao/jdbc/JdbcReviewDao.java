package com.miskevich.movieland.dao.jdbc;

import com.miskevich.movieland.dao.IReviewDao;
import com.miskevich.movieland.dao.jdbc.mapper.ReviewRowMapper;
import com.miskevich.movieland.entity.Movie;
import com.miskevich.movieland.entity.Review;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class JdbcReviewDao implements IReviewDao {

    private final Logger LOG = LoggerFactory.getLogger(getClass());
    private static final ReviewRowMapper REVIEW_ROW_MAPPER = new ReviewRowMapper();

    @Autowired
    private NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    @Autowired
    private String getReviewsByMovieIdSQL;
    @Autowired
    private String addReviewSQL;
    @Autowired
    private String updateReviewSQL;

    @Override
    public List<Review> getByMovieId(int movieId) {
        MapSqlParameterSource parameters = new MapSqlParameterSource();
        parameters.addValue("movieId", movieId);

        LOG.info("Start query to get reviews from DB by movieId");
        long startTime = System.currentTimeMillis();
        List<Review> reviews = namedParameterJdbcTemplate.query(getReviewsByMovieIdSQL, parameters, REVIEW_ROW_MAPPER);
        LOG.info("Finish query to get reviews from DB by movieId. It took {} ms", System.currentTimeMillis() - startTime);
        return reviews;
    }

    @Override
    public Review add(Review review) {
        MapSqlParameterSource parameters = new MapSqlParameterSource();
        parameters.addValue("movieId", review.getMovie().getId());
        parameters.addValue("userId", review.getUser().getId());
        parameters.addValue("description", review.getText());

        LOG.info("Start query to add review into DB: {}", review);
        long startTime = System.currentTimeMillis();
        KeyHolder keyHolder = new GeneratedKeyHolder();
        namedParameterJdbcTemplate.update(addReviewSQL, parameters, keyHolder);
        long reviewId = keyHolder.getKey().longValue();
        review.setId(reviewId);

        LOG.info("Finish query to add review into DB: {}. It took {} ms", review, System.currentTimeMillis() - startTime);
        return review;
    }

    @Override
    public void persist(Movie movie) {
        for (int i = 0; i < movie.getReviews().size(); i++) {
            add(movie.getReviews().get(i));
        }
    }

    @Override
    public void update(Movie movie) {
        for (int i = 0; i < movie.getReviews().size(); i++) {
            long reviewId = movie.getReviews().get(i).getId();
            int userId = movie.getReviews().get(i).getUser().getId();
            String description = movie.getReviews().get(i).getText();
            MapSqlParameterSource parameters = populateSQLParameters(reviewId, userId, description);

            LOG.info("Start query to update reviewId into DB: {}", reviewId);
            long startTime = System.currentTimeMillis();
            namedParameterJdbcTemplate.update(updateReviewSQL, parameters);
            LOG.info("Finish query to update reviewId into DB: {}. It took {} ms", reviewId, System.currentTimeMillis() - startTime);
        }
    }

    private MapSqlParameterSource populateSQLParameters(long reviewId, int userId, String description) {
        MapSqlParameterSource parameters = new MapSqlParameterSource();
        parameters.addValue("reviewId", reviewId);
        parameters.addValue("userId", userId);
        parameters.addValue("description", description);
        return parameters;
    }
}
