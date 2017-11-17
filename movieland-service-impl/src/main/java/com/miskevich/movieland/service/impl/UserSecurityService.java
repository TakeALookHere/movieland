package com.miskevich.movieland.service.impl;

import com.miskevich.movieland.entity.User;
import com.miskevich.movieland.service.exception.AuthRequiredException;
import com.miskevich.movieland.service.exception.UuidExpirationException;
import com.miskevich.movieland.service.security.UserPrincipal;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.util.IdGenerator;

import java.time.LocalDateTime;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class UserSecurityService {

    private final Logger LOG = LoggerFactory.getLogger(getClass());
    private static final Map<String, UserPrincipal> UUID_USER_CACHE = new ConcurrentHashMap<>();

    @Autowired
    private IdGenerator idGenerator;

    public String putUserIntoCache(User user) {
        String uuid = String.valueOf(idGenerator.generateId());
        UserPrincipal principal = new UserPrincipal(user, LocalDateTime.now().plusHours(2));
        UUID_USER_CACHE.put(uuid, principal);
        LOG.info("User's UUID was set for userId: " + user.getId());
        LOG.debug("User with UUID: " + user);
        return uuid;
    }

    public Optional<UserPrincipal> getFromCache(String uuid) {
        UserPrincipal principal = UUID_USER_CACHE.get(uuid);
        if (principal == null) {
            String message = "User with UUID " + uuid + " is not authorized, please login";
            LOG.warn(message);
            throw new AuthRequiredException(message);
        }
        if (principal.getUuidExpirationTime().isBefore(LocalDateTime.now())) {
            removeUserFromCache(uuid);
            String message = "UUID " + uuid + " has been expired for userId: " + principal.getUser().getId();
            LOG.warn(message);
            throw new UuidExpirationException(message);
        }
        return Optional.of(principal);
    }

    @Scheduled(initialDelayString = "${init.delay.user.cache}", fixedRateString = "${fixed.rate.user.cache}")
    private void clearUserCache() {
        UUID_USER_CACHE.forEach((k, v) -> {
            if (v.getUuidExpirationTime().isBefore(LocalDateTime.now())) {
                removeUserFromCache(k);
                LOG.info("User's UUID in cache was cleared for userId: " + v.getUser().getId());
            }
        });
    }

    public void removeUserFromCache(String uuid) {
        UUID_USER_CACHE.remove(uuid);
        LOG.info("User's UUID " + uuid + " was removed from cache");
    }
}
