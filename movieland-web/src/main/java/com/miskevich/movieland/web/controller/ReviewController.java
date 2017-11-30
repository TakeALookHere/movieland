package com.miskevich.movieland.web.controller;

import com.miskevich.movieland.entity.Review;
import com.miskevich.movieland.entity.User;
import com.miskevich.movieland.model.Role;
import com.miskevich.movieland.service.IReviewService;
import com.miskevich.movieland.service.security.UserPrincipal;
import com.miskevich.movieland.web.dto.ReviewDto;
import com.miskevich.movieland.web.json.JsonConverter;
import com.miskevich.movieland.web.json.ReviewDtoConverter;
import com.miskevich.movieland.web.security.RoleRequired;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping(produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
public class ReviewController {

    private final Logger LOG = LoggerFactory.getLogger(getClass());

    @Autowired
    private IReviewService reviewService;

    @ResponseBody
    @RequestMapping(value = "/review", method = RequestMethod.POST)
    @RoleRequired({Role.ADMIN, Role.USER})
    public String add(@RequestBody String reviewFromRequest, UserPrincipal principal) {
        ReviewDto reviewDto = JsonConverter.fromJson(reviewFromRequest, ReviewDto.class);
        Review review = ReviewDtoConverter.mapDtoIntoObject(reviewDto);
        review.setUser(new User(principal.getUser().getId()));

        LOG.info("Sending request to add review: {}", review);
        long startTime = System.currentTimeMillis();
        Review addedReview = reviewService.add(review);
        LOG.info("Review was added. It took {} ms", System.currentTimeMillis() - startTime);

        return JsonConverter.toJson(addedReview);
    }
}
