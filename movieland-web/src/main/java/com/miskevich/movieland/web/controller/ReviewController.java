package com.miskevich.movieland.web.controller;

import com.miskevich.movieland.entity.Review;
import com.miskevich.movieland.entity.User;
import com.miskevich.movieland.model.Role;
import com.miskevich.movieland.service.IReviewService;
import com.miskevich.movieland.service.IUserService;
import com.miskevich.movieland.service.exception.AuthRequiredException;
import com.miskevich.movieland.service.impl.UserSecurityService;
import com.miskevich.movieland.web.dto.ReviewDto;
import com.miskevich.movieland.web.exception.InvalidAccessException;
import com.miskevich.movieland.web.json.JsonConverter;
import com.miskevich.movieland.web.json.ReviewDtoConverter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedReader;
import java.io.IOException;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

@Controller
@RequestMapping(produces = "application/json;charset=UTF-8")
public class ReviewController {

    private final Logger LOG = LoggerFactory.getLogger(getClass());

    @Autowired
    private IReviewService reviewService;
    @Autowired
    private IUserService userService;
    @Autowired
    private UserController userController;
    @Autowired
    private UserSecurityService userSecurityService;

    @ResponseBody
    @RequestMapping(value = "/review", method = RequestMethod.POST)
    public void add(@RequestHeader("uuid") String uuid, HttpServletRequest request, HttpServletResponse response) {
        Optional<User> userFromCache = userSecurityService.getFromCache(uuid);
        //Map<UUID, User> uuidUserCache = userController.getUuidUserCache();
        //User userFromCache = uuidUserCache.get(uuid);

        if (!userFromCache.isPresent()) {
            String message = "User's uuid: " + uuid + " has been expired, please login";
            LOG.warn(message);
            throw new AuthRequiredException(message);
        } else {
            String role = userService.getRole(userFromCache.get().getId());
            try {
                Role roleByName = Role.getRoleByName(role);
                if (roleByName.equals(Role.USER)) {
                    ReviewDto reviewDto = getReviewFromRequest(request);
                    saveReview(userFromCache.get(), reviewDto);
                } else {
                    String message = "Validation of user's role access type failed, required role: USER";
                    LOG.warn(message);
                    throw new InvalidAccessException(message);
                }
            } catch (IllegalArgumentException e) {
                LOG.warn("Validation of user's role name failed", e);
                throw new IllegalArgumentException(e);
            }
        }
    }

    private void saveReview(User userFromCache, ReviewDto reviewDto) {
        Review review = ReviewDtoConverter.mapDtoIntoObject(reviewDto);
        review.setUser(new User(userFromCache.getId()));
        LOG.info("Sending request to add review");
        long startTime = System.currentTimeMillis();
        reviewService.add(review);
        LOG.info("Review was added. It took {} ms", System.currentTimeMillis() - startTime);
    }

    private ReviewDto getReviewFromRequest(HttpServletRequest request) {
        ReviewDto reviewDto;
        try (BufferedReader reader = request.getReader()) {
            reviewDto = JsonConverter.fromJson(reader, ReviewDto.class);
        } catch (IOException e) {
            LOG.error("ERROR: ", e);
            throw new RuntimeException(e);
        }
        return reviewDto;
    }

    private void redirectToLoginPage(UUID uuid, HttpServletResponse response) {
        try {
            LOG.info("Redirecting user with UUID " + uuid + " to login page...");
            response.sendRedirect("/v1/login");
        } catch (IOException e) {
            LOG.error("ERROR during redirection to login page", e);
            throw new RuntimeException(e);
        }
    }
}
