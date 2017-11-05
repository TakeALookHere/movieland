package com.miskevich.movieland.web.util;

import com.miskevich.movieland.web.dto.MovieDto;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.math.BigDecimal;
import java.math.RoundingMode;

public abstract class RateConverter {

    private final static Logger LOG = LoggerFactory.getLogger(RateConverter.class);

    public static MovieDto enrichMovieWithPriceForRate(MovieDto movieDto, Double rateValue) {
        double priceUAH = movieDto.getPrice();
        double priceForNewCurrency = priceUAH / rateValue;
        double roundedPrice = BigDecimal.valueOf(priceForNewCurrency).setScale(2, RoundingMode.HALF_UP).doubleValue();
        movieDto.setPrice(roundedPrice);
        LOG.info("New price for movie was set: " + roundedPrice);
        return movieDto;
    }
}
