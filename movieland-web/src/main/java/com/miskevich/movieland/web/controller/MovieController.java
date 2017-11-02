package com.miskevich.movieland.web.controller;

import com.miskevich.movieland.entity.Movie;
import com.miskevich.movieland.service.IMovieService;
import com.miskevich.movieland.web.dto.MovieDto;
import com.miskevich.movieland.web.json.DtoConverter;
import com.miskevich.movieland.web.json.JsonConverter;
import com.miskevich.movieland.web.model.SortingType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping(value = "/v1", produces = "application/json;charset=UTF-8")
public class MovieController {

    private final Logger LOG = LoggerFactory.getLogger(getClass());

    @Autowired
    private IMovieService movieService;

    @ResponseBody
    @RequestMapping(method = RequestMethod.GET, value = "/movie")
    public String getAllMovies(@RequestParam(value = "rating", required = false) String rating,
                               @RequestParam(value = "price", required = false) String price) {

        LOG.info("Sending request to get all movies");
        long startTime = System.currentTimeMillis();

        List<Movie> movies;

        if (rating != null && rating.equalsIgnoreCase(SortingType.DESC.getSortingType())) {
            movies = movieService.getAllRatingDesc();
        } else if (price != null && price.equalsIgnoreCase(SortingType.ASC.getSortingType())) {
            movies = movieService.getAllPriceAsc();
        } else if (price != null && price.equalsIgnoreCase(SortingType.DESC.getSortingType())) {
            movies = movieService.getAllPriceDesc();
        } else if(rating == null && price == null) {
            movies = movieService.getAll();
        }else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Incorrect sorting type in request parameters. Use DESC for ratings or ASC/DESC for price").toString();
        }

        List<MovieDto> movieDtos = DtoConverter.mapList(movies);
        String moviesJson = JsonConverter.toJson(movieDtos);

        LOG.info("Movies were received. JSON movies: {}. It took {} ms", moviesJson, System.currentTimeMillis() - startTime);
        return moviesJson;
    }

    @ResponseBody
    @RequestMapping(method = RequestMethod.GET, value = "/movie/random")
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
    @RequestMapping(method = RequestMethod.GET, value = "/movie/genre/{genreId}")
    public String getByGenre(@PathVariable int genreId) {
        LOG.info("Sending request to get movies by genre");
        long startTime = System.currentTimeMillis();
        List<Movie> movies = movieService.getByGenre(genreId);

        List<MovieDto> movieDtos = DtoConverter.mapList(movies);
        String moviesJson = JsonConverter.toJson(movieDtos);

        LOG.info("Movies by genre were received. JSON movies: {}. It took {} ms", moviesJson, System.currentTimeMillis() - startTime);
        return moviesJson;
    }

}
