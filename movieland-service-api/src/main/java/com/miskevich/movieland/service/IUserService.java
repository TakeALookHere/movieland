package com.miskevich.movieland.service;

import com.miskevich.movieland.entity.User;

public interface IUserService {

    User getByEmailAndPassword(String email, String password);

    String getRole(int id);
}
