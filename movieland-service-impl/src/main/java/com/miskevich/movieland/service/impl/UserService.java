package com.miskevich.movieland.service.impl;

import com.miskevich.movieland.dao.IUserDao;
import com.miskevich.movieland.entity.User;
import com.miskevich.movieland.service.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserService implements IUserService {

    @Autowired
    private IUserDao userDao;

    @Override
    public User getByEmailAndPassword(String email, String password) {
        return userDao.getByEmailAndPassword(email, password);
    }

    @Override
    public String getRole(int id) {
        return userDao.getRole(id);
    }
}
