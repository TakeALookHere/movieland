package com.miskevich.movieland.web.controller;

import com.miskevich.movieland.entity.User;
import com.miskevich.movieland.service.IUserService;
import com.miskevich.movieland.web.dto.UserDto;
import com.miskevich.movieland.web.json.JsonConverter;
import com.miskevich.movieland.web.json.UserDtoConverter;
import com.miskevich.movieland.web.util.UUIDRefresher;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.util.IdGenerator;
import org.springframework.util.JdkIdGenerator;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

@Controller
@CrossOrigin(origins = "http://localhost:3000")
@RequestMapping(produces = "application/json;charset=UTF-8")
public class UserController {

    private final Logger LOG = LoggerFactory.getLogger(getClass());
    private ScheduledExecutorService scheduledExecutorService = Executors.newScheduledThreadPool(1);
    private static final Map<UUID, User> UUID_USER_CACHE = new HashMap<>();

    @Autowired
    private IUserService userService;
    @Value("${init.delay.user.uuid}")
    private long initDelayUserUUID;
    private IdGenerator idGenerator = new JdkIdGenerator();

    @ResponseBody
    @RequestMapping(value = "/login", method = RequestMethod.POST)
    public String login(HttpServletRequest request, HttpServletResponse response) {

        User user;
        long startTime;
        try (BufferedReader reader = request.getReader()) {
            user = JsonConverter.fromJson(reader, User.class);

            LOG.info("Sending request to get user by email and password");
            startTime = System.currentTimeMillis();

            try {
                user = userService.getByEmailAndPassword(user.getEmail(), user.getPassword());
            } catch (EmptyResultDataAccessException e) {
                response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                String message = "No user in DB with such pair of email + password was found!";
                LOG.error(message);
                return null;
            }
        } catch (IOException e) {
            LOG.error("ERROR: ", e);
            throw new RuntimeException(e);
        }

        UUID uuid = idGenerator.generateId();
        UUID_USER_CACHE.put(uuid, user);
        user.setUuid(uuid);
        LOG.info("User's UUID was set");
        LOG.debug("User with UUID: " + user);

        UUIDRefresher uuidRefresher = new UUIDRefresher(UUID_USER_CACHE, uuid);
        scheduledExecutorService.schedule(uuidRefresher, initDelayUserUUID, TimeUnit.MINUTES);

        UserDto userDto = UserDtoConverter.mapObject(user);
        String userJson = JsonConverter.toJson(userDto);
        LOG.info("User was received. JSON movies: {}. It took {} ms", userJson, System.currentTimeMillis() - startTime);

        MDC.put("nickname", user.getNickname());
        LOG.info("Query get feedback by userId: " + user.getId() + " and UUID: " + uuid);

        return userJson;
    }

    @RequestMapping(value = "/logout", method = RequestMethod.DELETE)
    @ResponseStatus(value = HttpStatus.OK)
    public void logout(@RequestHeader("X-Request-ID") UUID uuid) {
        UUID_USER_CACHE.remove(uuid);
        LOG.info("User with UUID " + uuid + " performed logout");
    }
}
