package com.miskevich.movieland.web.controller;

import com.miskevich.movieland.entity.User;
import com.miskevich.movieland.service.IUserService;
import com.miskevich.movieland.web.dto.UserDto;
import com.miskevich.movieland.web.exception.InvalidUserException;
import com.miskevich.movieland.web.json.JsonConverter;
import com.miskevich.movieland.web.json.UserDtoConverter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Controller;
import org.springframework.util.IdGenerator;
import org.springframework.util.JdkIdGenerator;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.io.BufferedReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@Controller
@RequestMapping(produces = "application/json;charset=UTF-8")
public class UserController {

    private final Logger LOG = LoggerFactory.getLogger(getClass());
    private final Map<UUID, User> UUID_USER_CACHE = new HashMap<>();

    @Autowired
    private IUserService userService;
    //    @Value("${init.delay.user.uuid}")
    //    private long initDelayUserUUID;
    private IdGenerator idGenerator = new JdkIdGenerator();
    private UUID uuid;

    @ResponseBody
    @RequestMapping(value = "/login", method = RequestMethod.POST)
    public String login(HttpServletRequest request) {
        User user = getUserFromRequest(request);
        LOG.info("Sending request to get user by email and password");
        long startTime = System.currentTimeMillis();

        user = getUserFromDB(user);
        putUserIntoCache(user);

        UserDto userDto = UserDtoConverter.mapObject(user, uuid);
        String userJson = JsonConverter.toJson(userDto);
        LOG.info("User was received. JSON movies: {}. It took {} ms", userJson, System.currentTimeMillis() - startTime);

        MDC.put("nickname", user.getNickname());
        LOG.info("Query get feedback by userId: " + user.getId() + " and UUID: " + uuid);

        return userJson;
    }

    @ResponseBody
    @RequestMapping(value = "/login", method = RequestMethod.GET)
    public void login() {
        LOG.info("User was redirected to login page");
    }

    private void putUserIntoCache(User user) {
        uuid = idGenerator.generateId();
        UUID_USER_CACHE.put(uuid, user);
        LOG.info("User's UUID was set");
        LOG.debug("User with UUID: " + user);
    }

    private User getUserFromDB(User user) {
        String email = user.getEmail();
        String password = user.getPassword();

        try {
            user = userService.getByEmailAndPassword(email, password);
        } catch (EmptyResultDataAccessException e) {
            String message = "No user in DB with such pair of email and password was found: " + email + " and " + password;
            LOG.error(message);
            throw new InvalidUserException(message);
        }
        return user;
    }

    private User getUserFromRequest(HttpServletRequest request) {
        User user;
        try (BufferedReader reader = request.getReader()) {
            user = JsonConverter.fromJson(reader, User.class);
        } catch (IOException e) {
            LOG.error("ERROR: ", e);
            throw new RuntimeException(e);
        }
        return user;
    }

    @RequestMapping(value = "/logout", method = RequestMethod.DELETE)
    @ResponseStatus(value = HttpStatus.OK)
    public void logout(@RequestHeader("uuid") UUID uuid) {
        UUID_USER_CACHE.remove(uuid);
        LOG.info("User with UUID " + uuid + " performed logout");
    }

    @Scheduled(initialDelayString = "${init.delay.user.cache}", fixedRateString = "${fixed.rate.user.cache}")
    private void initCacheRefreshTask() {
        User removedUser = UUID_USER_CACHE.remove(uuid);
        if (removedUser != null) {
            LOG.info("User's UUID in cache was cleared for userId: " + removedUser.getId());
        }
    }

    public Map<UUID, User> getUuidUserCache() {
        Map<UUID, User> copy = new HashMap<>();
        for (Map.Entry<UUID, User> entry : UUID_USER_CACHE.entrySet()) {
            copy.put(entry.getKey(), entry.getValue());
        }
        return copy;
    }
}
