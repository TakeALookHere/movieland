package com.miskevich.movieland.web.controller;

import com.miskevich.movieland.dto.RateDto;
import com.miskevich.movieland.entity.Movie;
import com.miskevich.movieland.model.Currency;
import com.miskevich.movieland.model.Role;
import com.miskevich.movieland.model.SortingField;
import com.miskevich.movieland.model.SortingType;
import com.miskevich.movieland.service.IMovieService;
import com.miskevich.movieland.service.IUserService;
import com.miskevich.movieland.service.impl.RateService;
import com.miskevich.movieland.web.dto.MovieDto;
import com.miskevich.movieland.web.json.JsonConverter;
import com.miskevich.movieland.web.json.MovieDtoConverter;
import com.miskevich.movieland.web.security.RoleRequired;
import com.miskevich.movieland.web.util.RateConverter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping(produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
public class MovieController {

    private final Logger LOG = LoggerFactory.getLogger(getClass());

    @Autowired
    private IMovieService movieService;
    @Autowired
    private RateService rateService;
    @Autowired
    private IUserService userService;

    @ResponseBody
    @RequestMapping(value = "/movie", method = RequestMethod.GET)
    public String getAll(@RequestParam(required = false) LinkedHashMap<String, String> params) {
        Map<SortingField, SortingType> sortingFieldSortingTypeMap = validateInputParameters(params);
        LOG.info("Sending request to get all movies");
        long startTime = System.currentTimeMillis();

        List<Movie> movies = movieService.getAll(sortingFieldSortingTypeMap);

        List<MovieDto> movieDtos = MovieDtoConverter.mapList(movies);
        String moviesJson = JsonConverter.toJson(movieDtos);
        LOG.info("Movies were received. JSON movies: {}. It took {} ms", moviesJson, System.currentTimeMillis() - startTime);
        return moviesJson;
    }

    @ResponseBody
    @RequestMapping(value = "/movie", method = RequestMethod.POST)
    @RoleRequired(Role.ADMIN)
    public String add(@RequestBody String movieFromRequest) {
        LOG.info("Sending request to add movie");
        long startTime = System.currentTimeMillis();
        MovieDto movieDto = JsonConverter.fromJson(movieFromRequest, MovieDto.class);
        Movie movie = MovieDtoConverter.mapDtoIntoObject(movieDto);

        Movie movieAfterSave = movieService.persist(movie);

        movieDto = MovieDtoConverter.mapObject(movieAfterSave);
        String movieJson = JsonConverter.toJson(movieDto);
        LOG.info("Movie {} was added. It took {} ms", movieJson, System.currentTimeMillis() - startTime);
        return movieJson;
    }

    @ResponseBody
    @RequestMapping(value = "/movie/{movieId}", method = RequestMethod.PUT)
    @RoleRequired(Role.ADMIN)
    public String update(@RequestBody String movieFromRequest, @PathVariable int movieId) {
        LOG.info("Sending request to persist movie by id");
        long startTime = System.currentTimeMillis();
        MovieDto movieDto = JsonConverter.fromJson(movieFromRequest, MovieDto.class);
        Movie movie = MovieDtoConverter.mapDtoIntoObject(movieDto);
        movie.setId(movieId);

        Movie movieAfterUpdate = movieService.update(movie);

        movieDto = MovieDtoConverter.mapObject(movieAfterUpdate);
        String movieJson = JsonConverter.toJson(movieDto);
        LOG.info("Movie after persist was received. JSON movie: {}. It took {} ms", movieJson, System.currentTimeMillis() - startTime);
        return movieJson;
    }

    @ResponseBody
    @RequestMapping(value = "/movie/random", method = RequestMethod.GET)
    public String getThreeRandomMovies() {
        LOG.info("Sending request to get 3 random movies");
        long startTime = System.currentTimeMillis();
        List<Movie> movies = movieService.getThreeRandomMovies();

        List<MovieDto> movieDtos = MovieDtoConverter.mapList(movies);
        String moviesJson = JsonConverter.toJson(movieDtos);

        LOG.info("Random movies were received. JSON movies: {}. It took {} ms", moviesJson, System.currentTimeMillis() - startTime);
        return moviesJson;
    }

    @ResponseBody
    @RequestMapping(value = "/movie/genre/{genreId}", method = RequestMethod.GET)
    public String getByGenre(@PathVariable int genreId,
                             @RequestParam(required = false) LinkedHashMap<String, String> params) {
        Map<SortingField, SortingType> sortingFieldSortingTypeMap = validateInputParameters(params);
        LOG.info("Sending request to get movies by genre");
        long startTime = System.currentTimeMillis();
        List<Movie> movies = movieService.getByGenre(genreId, sortingFieldSortingTypeMap);

        List<MovieDto> movieDtos = MovieDtoConverter.mapList(movies);
        String moviesJson = JsonConverter.toJson(movieDtos);

        LOG.info("Movies by genre were received. JSON movies: {}. It took {} ms", moviesJson, System.currentTimeMillis() - startTime);
        return moviesJson;
    }

    @ResponseBody
    @RequestMapping(value = "/movie/{movieId}", method = RequestMethod.GET)
    public String getById(@PathVariable int movieId, @RequestParam(required = false, name = "currency") String currency) {
        LOG.info("Sending request to get movie by id");
        long startTime = System.currentTimeMillis();
        Movie movie = movieService.getById(movieId);

        MovieDto movieDto = MovieDtoConverter.mapObject(movie);

        if (currency != null) {
            validateCurrencyCode(currency);
            List<RateDto> rates = rateService.getAll();
            Double rateValue = getRateValue(rates, currency);
            RateConverter.enrichMovieWithPriceForRate(movieDto, rateValue);
        }

        String movieJson = JsonConverter.toJson(movieDto);

        LOG.info("Movie by id was received. JSON movie: {}. It took {} ms", movieJson, System.currentTimeMillis() - startTime);
        return movieJson;
    }

    private Double getRateValue(List<RateDto> rates, String currency) {
        for (RateDto rateDto : rates) {
            if (rateDto.getCurrencyName().equalsIgnoreCase(currency)) {
                double rate = rateDto.getRate();
                LOG.info("For currency = " + currency + " today's rate value is: " + rate);
                return rate;
            }
        }
        return null;
    }

    private void validateCurrencyCode(String currency) {
        try {
            LOG.info("Start validate parameter \"currency\" from request fot movie");
            Currency.getCurrencyByName(currency);
            LOG.info("Finish validate parameter \"currency\" from request for movie");
        } catch (IllegalArgumentException e) {
            LOG.warn("Invalid CCY code in request parameter", e);
            throw new RuntimeException(e);
        }
    }

    private Map<SortingField, SortingType> validateInputParameters(LinkedHashMap<String, String> params) {
        Map<SortingField, SortingType> sortingFieldSortingTypeMap = new LinkedHashMap<>();
        if (params.isEmpty()) {
            return sortingFieldSortingTypeMap;
        }
        LOG.info("Start validate parameters from request for sorting movies");
        try {
            for (Map.Entry<String, String> param : params.entrySet()) {
                SortingField sortingFieldByName = SortingField.getSortingFieldByName(param.getKey());
                SortingType sortingTypeByName = SortingType.getSortingTypeByName(param.getValue());
                sortingFieldSortingTypeMap.put(sortingFieldByName, sortingTypeByName);
                LOG.info("Finish validate parameters from request for sorting movies");
            }
        } catch (IllegalArgumentException e) {
            LOG.warn("Validation for input parameters for sorting movies failed", e);
            throw new RuntimeException(e);
        }
        return sortingFieldSortingTypeMap;
    }
}
