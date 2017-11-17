package com.miskevich.movieland.dao.jdbc;

import com.miskevich.movieland.dao.IUserDao;
import com.miskevich.movieland.dao.jdbc.mapper.RoleRowMapper;
import com.miskevich.movieland.dao.jdbc.mapper.UserRowMapper;
import com.miskevich.movieland.entity.User;
import com.miskevich.movieland.model.Role;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

@Repository
public class JdbcUserDao implements IUserDao {

    private final Logger LOG = LoggerFactory.getLogger(getClass());
    private static final UserRowMapper USER_ROW_MAPPER = new UserRowMapper();
    private static final RoleRowMapper ROLE_ROW_MAPPER = new RoleRowMapper();

    @Autowired
    private NamedParameterJdbcTemplate namedParameterJdbcTemplate;
    @Autowired
    private String getUserByEmailAndPasswordSQL;
    @Autowired
    private String getUserRoleSQL;

    @Override
    public User getByEmailAndPassword(String email, String password) {
        MapSqlParameterSource parameters = new MapSqlParameterSource();
        parameters.addValue("email", email);
        parameters.addValue("password", password);

        LOG.info("Start query to get user from DB by email and password");
        long startTime = System.currentTimeMillis();
        User user = namedParameterJdbcTemplate.queryForObject(getUserByEmailAndPasswordSQL, parameters, USER_ROW_MAPPER);
        LOG.info("Finish query to get user from DB by email and password. It took {} ms", System.currentTimeMillis() - startTime);
        return user;
    }

    @Override
    public Role getRole(int id) {
        MapSqlParameterSource parameters = new MapSqlParameterSource();
        parameters.addValue("userId", id);

        LOG.info("Start query to get user's role from DB");
        long startTime = System.currentTimeMillis();
        Role role = namedParameterJdbcTemplate.queryForObject(getUserRoleSQL, parameters, ROLE_ROW_MAPPER);
        LOG.info("Finish query to get user's role from DB. It took {} ms", System.currentTimeMillis() - startTime);
        return role;
    }
}
