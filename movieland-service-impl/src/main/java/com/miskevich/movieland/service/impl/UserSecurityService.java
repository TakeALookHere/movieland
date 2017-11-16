package com.miskevich.movieland.service.impl;

import com.miskevich.movieland.entity.User;
import com.miskevich.movieland.service.exception.AuthRequiredException;
import com.miskevich.movieland.service.exception.UuidExpirationException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.util.IdGenerator;

import java.time.LocalDateTime;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class UserSecurityService {

    private final Logger LOG = LoggerFactory.getLogger(getClass());
    private static final Map<String, Map<User, LocalDateTime>> UUID_USER_CACHE = new ConcurrentHashMap<>();

    @Autowired
    private IdGenerator idGenerator;

    public String putUserIntoCache(User user) {
        String uuid = String.valueOf(idGenerator.generateId());
        Map<User, LocalDateTime> userLocalDateTimeMap = new HashMap<>();
        userLocalDateTimeMap.put(user, LocalDateTime.now());
        UUID_USER_CACHE.put(uuid, userLocalDateTimeMap);
        LOG.info("User's UUID was set for userId: " + user.getId());
        LOG.debug("User with UUID: " + user);
        return uuid;
    }

    public Optional<User> getFromCache(String uuid) {
        Map<User, LocalDateTime> userLocalDateMap = UUID_USER_CACHE.get(uuid);
        if (userLocalDateMap == null) {
            String message = "User with UUID " + uuid + " is not authorized, please login";
            LOG.warn(message);
            throw new AuthRequiredException(message);
        }

        for (Map.Entry<User, LocalDateTime> entry : userLocalDateMap.entrySet()) {
            LocalDateTime time = entry.getValue();
            User user = entry.getKey();
            if (time.isBefore(LocalDateTime.now().minusMinutes(1))) {
                removeUserFromCache(uuid);
                String message = "UUID " + uuid + " has been expired for userId: " + user.getId();
                LOG.warn(message);
                throw new UuidExpirationException(message);
            }
            return Optional.of(user);
        }
        return Optional.empty();
    }

    @Scheduled(initialDelayString = "${init.delay.user.cache}", fixedRateString = "${fixed.rate.user.cache}")
    private void clearUserCache() {
        for (Map.Entry<String, Map<User, LocalDateTime>> entry : UUID_USER_CACHE.entrySet()) {
            String uuid = entry.getKey();
            Map<User, LocalDateTime> userLocalDateMap = entry.getValue();

            userLocalDateMap.forEach((k, v) -> {
                if (v.isBefore(LocalDateTime.now().minusMinutes(1))) {
                    removeUserFromCache(uuid);
                    LOG.info("User's UUID in cache was cleared for userId: " + k.getId());
                }
            });
        }
    }

    public void removeUserFromCache(String uuid) {
        UUID_USER_CACHE.remove(uuid);
        LOG.info("User's UUID " + uuid + " was removed from cache");
    }
}
