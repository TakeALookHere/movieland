package com.miskevich.movieland.dao.jdbc;

import com.miskevich.movieland.dao.IReviewDao;
import com.miskevich.movieland.dao.jdbc.mapper.ReviewRowMapper;
import com.miskevich.movieland.entity.Review;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
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
}
