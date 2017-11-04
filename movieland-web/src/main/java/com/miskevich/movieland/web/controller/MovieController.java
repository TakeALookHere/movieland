package com.miskevich.movieland.web.controller;

import com.miskevich.movieland.entity.Movie;
import com.miskevich.movieland.model.Currency;
import com.miskevich.movieland.model.SortingField;
import com.miskevich.movieland.model.SortingType;
import com.miskevich.movieland.service.IMovieService;
import com.miskevich.movieland.web.dto.MovieDto;
import com.miskevich.movieland.web.dto.RateDto;
import com.miskevich.movieland.web.json.DtoConverter;
import com.miskevich.movieland.web.json.JsonConverter;
import com.miskevich.movieland.web.util.RateReader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@CrossOrigin(origins = "http://localhost:3000")
@Controller
@RequestMapping(produces = "application/json;charset=UTF-8", method = RequestMethod.GET)
public class MovieController {

    private final Logger LOG = LoggerFactory.getLogger(getClass());

    @Autowired
    private IMovieService movieService;
    @Autowired
    private RateReader rateReader;

    @ResponseBody
    @RequestMapping(value = "/movie")
    public String getAllMovies(@RequestParam(required = false) LinkedHashMap<String, String> params) {
        validateInputParameters(params);

        LOG.info("Sending request to get all movies");
        long startTime = System.currentTimeMillis();

        List<Movie> movies = movieService.getAll(params);

        List<MovieDto> movieDtos = DtoConverter.mapList(movies);
        String moviesJson = JsonConverter.toJson(movieDtos);

        LOG.info("Movies were received. JSON movies: {}. It took {} ms", moviesJson, System.currentTimeMillis() - startTime);
        return moviesJson;
    }

    private void validateInputParameters(LinkedHashMap<String, String> params) {
        LOG.info("Start validate parameters from request");
        try {
            for (Map.Entry<String, String> param : params.entrySet()) {
                String paramName = param.getKey();
                SortingField.getSortingFieldByName(paramName);
                SortingType.getSortingTypeByName(param.getValue());
                LOG.info("Finish validate parameters from request");
            }
        } catch (IllegalArgumentException e) {
            LOG.error("ERROR", e);
            throw new RuntimeException(e);
        }
    }

    @ResponseBody
    @RequestMapping(value = "/movie/random")
    public String getThreeRandomMovies() {
        LOG.info("Sending request to get 3 random movies");
        long startTime = System.currentTimeMillis();
        List<Movie> movies = movieService.getThreeRandomMovies();

        List<MovieDto> movieDtos = DtoConverter.mapList(movies);
        String moviesJson = JsonConverter.toJson(movieDtos);

        LOG.info("Random movies were received. JSON movies: {}. It took {} ms", moviesJson, System.currentTimeMillis() - startTime);
        return moviesJson;
    }

    @ResponseBody
    @RequestMapping(value = "/movie/genre/{genreId}")
    public String getByGenre(@PathVariable int genreId, @RequestParam(required = false) LinkedHashMap<String, String> params) {
        validateInputParameters(params);

        LOG.info("Sending request to get movies by genre");
        long startTime = System.currentTimeMillis();
        List<Movie> movies = movieService.getByGenre(genreId, params);

        List<MovieDto> movieDtos = DtoConverter.mapList(movies);
        String moviesJson = JsonConverter.toJson(movieDtos);

        LOG.info("Movies by genre were received. JSON movies: {}. It took {} ms", moviesJson, System.currentTimeMillis() - startTime);
        return moviesJson;
    }

    @ResponseBody
    @RequestMapping(value = "/movie/{movieId}")
    public String getById(@PathVariable int movieId, @RequestParam(required = false, name = "currency") String currency) {
        LOG.info("Sending request to get movie by id");
        long startTime = System.currentTimeMillis();
        Movie movie = movieService.getById(movieId);

        MovieDto movieDto = DtoConverter.mapObject(movie);

        if (currency != null) {
            validateCurrencyCode(currency);
            List<RateDto> rates = rateReader.getCurrentRates();
            Double rateValue = getRateValue(rates, currency);
            enrichMovieWithPriceForRate(movieDto, rateValue);
        }

        String movieJson = JsonConverter.toJson(movieDto);

        LOG.info("Movie by id was received. JSON movie: {}. It took {} ms", movieJson, System.currentTimeMillis() - startTime);
        return movieJson;
    }

    MovieDto enrichMovieWithPriceForRate(MovieDto movieDto, Double rateValue) {
        double priceUAH = movieDto.getPrice();
        double priceForNewCurrency = priceUAH / rateValue;
        double roundedPrice = BigDecimal.valueOf(priceForNewCurrency).setScale(2, RoundingMode.HALF_UP).doubleValue();
        movieDto.setPrice(roundedPrice);
        LOG.info("New price for movie was set: " + roundedPrice);
        return movieDto;
    }

    private Double getRateValue(List<RateDto> rates, String currency) {
        for (RateDto rateDto : rates) {
            if (rateDto.getCc().equalsIgnoreCase(currency)) {
                double rate = rateDto.getRate();
                LOG.info("For currency = " + currency + " today's rate value is: " + rate);
                return rate;
            }
        }
        return null;
    }

    private void validateCurrencyCode(String currency) {
        try {
            LOG.info("Start validate parameter \"currency\" from request");
            Currency.getCurrencyByName(currency);
            LOG.info("Finish validate parameter \"currency\" from request");
        } catch (IllegalArgumentException e) {
            LOG.error("ERROR", e);
            throw new RuntimeException(e);
        }
    }

}
