package com.miskevich.movieland.web.controller;

import com.miskevich.movieland.entity.Movie;
import com.miskevich.movieland.model.SortingField;
import com.miskevich.movieland.model.SortingType;
import com.miskevich.movieland.service.IMovieService;
import com.miskevich.movieland.web.dto.MovieDto;
import com.miskevich.movieland.web.json.DtoConverter;
import com.miskevich.movieland.web.json.JsonConverter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

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

}
