package com.miskevich.movieland.dao;

import com.miskevich.movieland.entity.User;
import com.miskevich.movieland.model.Role;

public interface IUserDao {

    User getByEmailAndPassword(String email, String password);

    Role getRole(int id);
}
