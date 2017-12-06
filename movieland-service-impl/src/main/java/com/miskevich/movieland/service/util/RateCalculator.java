package com.miskevich.movieland.service.util;

import com.miskevich.movieland.model.MovieRating;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.math.BigDecimal;
import java.math.RoundingMode;

public abstract class RateCalculator {
    private static final Logger LOG = LoggerFactory.getLogger(RateCalculator.class);

    public static double calculate(MovieRating userRating, double currentRating, long newVotesCount) {
        double newRatingNotRounded = (currentRating + userRating.getRating()) / newVotesCount;
        double newRating = BigDecimal.valueOf(newRatingNotRounded).setScale(1, RoundingMode.HALF_UP).doubleValue();
        LOG.debug("New rating for movieId {} was calculated based on current voting: {}", userRating.getMovie().getId(), newRating);
        return newRating;
    }
}
