package com.miskevich.movieland.web.controller;

import com.miskevich.movieland.entity.Review;
import com.miskevich.movieland.entity.User;
import com.miskevich.movieland.model.Role;
import com.miskevich.movieland.service.IReviewService;
import com.miskevich.movieland.service.IUserService;
import com.miskevich.movieland.service.exception.AuthRequiredException;
import com.miskevich.movieland.service.security.UserPrincipal;
import com.miskevich.movieland.web.dto.ReviewDto;
import com.miskevich.movieland.web.exception.InvalidAccessException;
import com.miskevich.movieland.web.json.JsonConverter;
import com.miskevich.movieland.web.json.ReviewDtoConverter;
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
    @Autowired
    private IUserService userService;

    @ResponseBody
    @RequestMapping(value = "/review", method = RequestMethod.POST)
    public String add(@RequestBody String review, UserPrincipal principal) {
        if (principal != null) {
            Role role = userService.getRole(principal.getUser().getId());
            if (!(role.equals(Role.USER) || role.equals(Role.ADMIN))) {
                String message = "Validation of user's role access type failed, required role: USER/ADMIN";
                LOG.warn(message);
                throw new InvalidAccessException(message);
            }

            ReviewDto reviewDto = JsonConverter.fromJson(review, ReviewDto.class);
            Review addedReview = saveReview(principal, reviewDto);
            return JsonConverter.toJson(addedReview);
        }else {
            String message = "Request header doesn't contain uuid";
            LOG.warn(message);
            throw new AuthRequiredException(message);
        }
    }

    private Review saveReview(UserPrincipal principal, ReviewDto reviewDto) {
        Review review = ReviewDtoConverter.mapDtoIntoObject(reviewDto);
        review.setUser(new User(principal.getUser().getId()));
        LOG.info("Sending request to add review: {}", review);
        long startTime = System.currentTimeMillis();
        Review addedReview = reviewService.add(review);
        LOG.info("Review was added. It took {} ms", System.currentTimeMillis() - startTime);
        return addedReview;
    }
}
