package com.miskevich.movieland.web.controller;

import com.miskevich.movieland.entity.Country;
import com.miskevich.movieland.service.ICountryService;
import com.miskevich.movieland.web.json.JsonConverter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

@RequestMapping(produces = MediaType.APPLICATION_JSON_UTF8_VALUE, method = RequestMethod.GET)
@Controller
public class CountryController {

    private final Logger LOG = LoggerFactory.getLogger(getClass());

    @Autowired
    private ICountryService countryService;

    @ResponseBody
    @RequestMapping(value = "/country")
    public String getAllGenres() {
        LOG.info("Sending request to get all countries");
        long startTime = System.currentTimeMillis();
        List<Country> countries = countryService.getAll();
        String countriesJson = JsonConverter.toJson(countries);
        LOG.info("Countries were received. JSON countries: {}. It took {} ms", countriesJson, System.currentTimeMillis() - startTime);
        return countriesJson;
    }
}
