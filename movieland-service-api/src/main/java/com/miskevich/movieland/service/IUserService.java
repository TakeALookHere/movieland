package com.miskevich.movieland.service;

import com.miskevich.movieland.entity.User;
import com.miskevich.movieland.model.Role;

public interface IUserService {

    User getByEmailAndPassword(String email, String password);

    Role getRole(int id);
}
