package com.miskevich.movieland.service.security;

import com.miskevich.movieland.entity.User;

import java.security.Principal;
import java.time.LocalDateTime;

public class UserPrincipal implements Principal {
    private User user;
    private LocalDateTime uuidExpirationTime;

    public UserPrincipal(User user, LocalDateTime uuidExpirationTime) {
        this.user = user;
        this.uuidExpirationTime = uuidExpirationTime;
    }

    @Override
    public String getName() {
        return user.getNickname();
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public LocalDateTime getUuidExpirationTime() {
        return uuidExpirationTime;
    }

    public void setUuidExpirationTime(LocalDateTime uuidExpirationTime) {
        this.uuidExpirationTime = uuidExpirationTime;
    }
}