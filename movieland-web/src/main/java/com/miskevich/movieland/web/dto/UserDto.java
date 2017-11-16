package com.miskevich.movieland.web.dto;

public class UserDto {

    private String nickname;
    private String uuid;

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
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
