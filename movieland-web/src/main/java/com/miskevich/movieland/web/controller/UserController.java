package com.miskevich.movieland.web.controller;

import com.miskevich.movieland.entity.User;
import com.miskevich.movieland.service.IUserService;
import com.miskevich.movieland.service.impl.UserSecurityService;
import com.miskevich.movieland.web.dto.UserDto;
import com.miskevich.movieland.web.exception.InvalidUserException;
import com.miskevich.movieland.web.json.JsonConverter;
import com.miskevich.movieland.web.json.UserDtoConverter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@Controller
@RequestMapping(produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
public class UserController {

    private final Logger LOG = LoggerFactory.getLogger(getClass());
    @Autowired
    private IUserService userService;
    @Autowired
    private UserSecurityService userSecurityService;

    @ResponseBody
    @RequestMapping(value = "/login", method = RequestMethod.POST)
    public String login(@RequestBody Map<String, String> userCredentials) {
        LOG.info("Sending request to get user by email and password");
        long startTime = System.currentTimeMillis();

        User user = authAndEnrich(userCredentials);
        String uuid = userSecurityService.putUserIntoCache(user);

        UserDto userDto = UserDtoConverter.mapObject(user, uuid);
        String userJson = JsonConverter.toJson(userDto);
        LOG.info("User was received. JSON movies: {}. It took {} ms", userJson, System.currentTimeMillis() - startTime);
        LOG.info("Query get feedback by userId: " + user.getId() + " and UUID: " + uuid);

        return userJson;
    }

    User authAndEnrich(Map<String, String> userCredentials) {
        String email = userCredentials.get("email");
        String password = userCredentials.get("password");
        User user;
        try {
            user = userService.getByEmailAndPassword(email, password);
        } catch (EmptyResultDataAccessException e) {
            String message = "No user in DB with such pair of email and password was found: " + email + " and " + password;
            LOG.error(message);
            throw new InvalidUserException(message);
        }
        return user;
    }

    @RequestMapping(value = "/logout", method = RequestMethod.DELETE)
    @ResponseStatus(value = HttpStatus.OK)
    public void logout(@RequestHeader("uuid") String uuid) {
        userSecurityService.removeUserFromCache(uuid);
        LOG.info("User with UUID " + uuid + " performed logout");
    }
}
