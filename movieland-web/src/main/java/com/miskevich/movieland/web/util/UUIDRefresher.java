package com.miskevich.movieland.web.util;

import com.miskevich.movieland.entity.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;
import java.util.UUID;

public class UUIDRefresher implements Runnable {

    private final Logger LOG = LoggerFactory.getLogger(getClass());
    private Map<UUID, User> uuidUserMap;
    private UUID uuid;

    public UUIDRefresher(Map<UUID, User> uuidUserMap, UUID uuid) {
        this.uuidUserMap = uuidUserMap;
        this.uuid = uuid;
    }

    @Override
    public void run() {
        User removedUser = uuidUserMap.remove(uuid);
        if (removedUser != null) {
            LOG.info("User's UUID in cache was cleared for userId: " + removedUser.getId());
        }

        System.out.println("!!!!!!CACHE CLEARED");
        for (Map.Entry entry : uuidUserMap.entrySet()) {
            System.out.println(entry.getKey());
            System.out.println(entry.getValue());
        }
    }
}
