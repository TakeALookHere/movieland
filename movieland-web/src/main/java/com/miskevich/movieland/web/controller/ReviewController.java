package com.miskevich.movieland.web.controller;

import com.miskevich.movieland.entity.Review;
import com.miskevich.movieland.service.IReviewService;
import com.miskevich.movieland.web.json.JsonConverter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.io.BufferedReader;
import java.io.IOException;
import java.util.UUID;

@Controller
@RequestMapping(produces = "application/json;charset=UTF-8")
public class ReviewController {

    private final Logger LOG = LoggerFactory.getLogger(getClass());

    @Autowired
    private IReviewService reviewService;

    @ResponseBody
    @RequestMapping(value = "/review", method = RequestMethod.POST)
    public void add(@RequestHeader("X-Request-ID") UUID uuid, HttpServletRequest request) {

        //Get User from UUID_USER_CACHE by uuid

        //Check user's role (should be USER)


        Review review;

        try (BufferedReader reader = request.getReader()) {
            review = JsonConverter.fromJson(reader, Review.class);
        } catch (IOException e) {
            LOG.error("ERROR: ", e);
            throw new RuntimeException(e);
        }

        LOG.info("Sending request to add review");
        long startTime = System.currentTimeMillis();
        reviewService.add(review);
        LOG.info("Review was added. It took {} ms", System.currentTimeMillis() - startTime);
    }
}
