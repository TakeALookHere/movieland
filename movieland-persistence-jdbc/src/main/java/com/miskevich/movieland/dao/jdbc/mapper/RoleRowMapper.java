package com.miskevich.movieland.dao.jdbc.mapper;

import com.miskevich.movieland.model.Role;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class RoleRowMapper implements RowMapper<Role> {

    @Override
    public Role mapRow(ResultSet resultSet, int rowNum) throws SQLException {
        return Role.getRoleByName(resultSet.getString("name"));
    }
}
