package com.miskevich.movieland.web.controller;

import com.miskevich.movieland.entity.Movie;
import com.miskevich.movieland.service.IMovieService;
import com.miskevich.movieland.web.json.JsonConverter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

@Controller
@RequestMapping("/v1")
public class MovieController {

    private final Logger LOG = LoggerFactory.getLogger(getClass());

    @Autowired
    private IMovieService movieService;

    @ResponseBody
    @RequestMapping(method = RequestMethod.GET, value = "/movie", produces = "application/json;charset=UTF-8")
    public String getAllMovies() {
        LOG.info("Sending request to get all movies");
        long startTime = System.currentTimeMillis();
        List<Movie> movies = movieService.getAll();
        String moviesJson = JsonConverter.toJson(movies);
        LOG.info("Movies were received. It took {} ms", moviesJson, System.currentTimeMillis() - startTime);
        return moviesJson;
    }
}
