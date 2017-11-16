package com.miskevich.movieland.web.security;

import java.security.Principal;

public class UserPrincipal implements Principal {
    private int id;
    private String nickname;

    public UserPrincipal(int id, String nickname) {
        this.id = id;
        this.nickname = nickname;
    }

    @Override
    public String getName() {
        return nickname;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }
}