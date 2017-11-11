package com.miskevich.movieland.dao.jdbc.mapper;

import com.miskevich.movieland.entity.User;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class UserRowMapper implements RowMapper<User> {

    @Override
    public User mapRow(ResultSet resultSet, int rowNum) throws SQLException {
        return new User(resultSet.getInt("id"),
                resultSet.getString("nickname"),
                resultSet.getString("email"));
    }
}
