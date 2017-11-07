package com.miskevich.movieland.web.util;

import com.miskevich.movieland.entity.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;
import java.util.UUID;

public class UUIDRefresher implements Runnable {

    private final Logger LOG = LoggerFactory.getLogger(getClass());
    private Map<UUID, User> uuidUserCache;
    private UUID uuid;

    public UUIDRefresher(Map<UUID, User> uuidUserCache, UUID uuid) {
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
