package com.miskevich.movieland.web.dto;

import java.util.UUID;

public class UserDto {

    private String nickname;
    private UUID uuid;

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public UUID getUuid() {
        return uuid;
    }

    public void setUuid(UUID uuid) {
        this.uuid = uuid;
    }

    @Override
    public String toString() {
        return "UserDto{" +
                "nickname='" + nickname + '\'' +
                ", uuid=" + uuid +
                '}';
    }
}
