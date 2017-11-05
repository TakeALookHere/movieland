package com.miskevich.movieland.dao;

import com.miskevich.movieland.entity.User;

public interface IUserDao {

    User getByEmailAndPassword(String email, String password);
}
