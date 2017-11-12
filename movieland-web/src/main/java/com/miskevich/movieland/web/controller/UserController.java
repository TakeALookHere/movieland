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
import org.springframework.scheduling.TaskScheduler;
import org.springframework.stereotype.Controller;
import org.springframework.util.IdGenerator;
import org.springframework.util.JdkIdGenerator;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.io.BufferedReader;
import java.io.IOException;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

@Controller
@RequestMapping(produces = "application/json;charset=UTF-8")
public class UserController {

    private final Logger LOG = LoggerFactory.getLogger(getClass());
    private final Map<UUID, User> UUID_USER_CACHE = new ConcurrentHashMap<>();

    @Autowired
    private IUserService userService;
    @Autowired
    private TaskScheduler webCacheScheduler;
    private IdGenerator idGenerator = new JdkIdGenerator();

    @ResponseBody
    @RequestMapping(value = "/login", method = RequestMethod.POST)
    public String login(HttpServletRequest request) {
        User user = getUserFromRequest(request);
        LOG.info("Sending request to get user by email and password");
        long startTime = System.currentTimeMillis();

        user = getUserFromDB(user);
        UUID uuid = putUserIntoCache(user);

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

    private UUID putUserIntoCache(User user) {
        UUID uuid = idGenerator.generateId();
        UUID_USER_CACHE.put(uuid, user);
        LOG.info("User's UUID was set for userId: " + user.getId());
        LOG.debug("User with UUID: " + user);

        webCacheScheduler.schedule(new CacheCleaner(UUID_USER_CACHE, uuid),
                Instant.now().plus(2, ChronoUnit.HOURS));
        LOG.info("Task for cache cleaning was scheduled for userId: " + user.getId());
        return uuid;
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

    public Map<UUID, User> getUuidUserCache() {
        Map<UUID, User> copy = new HashMap<>();
        for (Map.Entry<UUID, User> entry : UUID_USER_CACHE.entrySet()) {
            copy.put(entry.getKey(), entry.getValue());
        }
        return copy;
    }

    class CacheCleaner implements Runnable {

        private Map<UUID, User> uuidUserCache;
        private UUID uuid;

        CacheCleaner(Map<UUID, User> uuidUserCache, UUID uuid) {
            this.uuidUserCache = uuidUserCache;
            this.uuid = uuid;
        }

        @Override
        public void run() {
            User removedUser = uuidUserCache.remove(uuid);
            if (removedUser != null) {
                LOG.info("User's UUID in cache was cleared for userId: " + removedUser.getId());
            }
        }
    }
}
