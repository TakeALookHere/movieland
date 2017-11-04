package com.miskevich.movieland.web.controller;

import com.miskevich.movieland.entity.Genre;
import com.miskevich.movieland.service.IGenreService;
import com.miskevich.movieland.web.json.JsonConverter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

@CrossOrigin(origins = "http://localhost:3000")
@Controller
public class GenreController {

    private final Logger LOG = LoggerFactory.getLogger(getClass());

    @Autowired
    private IGenreService genreService;

    @ResponseBody
    @RequestMapping(method = RequestMethod.GET, value = "/genre", produces = "application/json;charset=UTF-8")
    public String getAllGenres() {
        LOG.info("Sending request to get all genres");
        long startTime = System.currentTimeMillis();
        List<Genre> genres = genreService.getAll();
        String genresJson = JsonConverter.toJson(genres);
        LOG.info("Genres were received. JSON genres: {}. It took {} ms", genresJson, System.currentTimeMillis() - startTime);
        return genresJson;
    }
}