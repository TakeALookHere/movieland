package com.miskevich.movieland.dao.jdbc;

import com.miskevich.movieland.dao.ICountryDao;
import com.miskevich.movieland.dao.jdbc.mapper.CountryRowMapper;
import com.miskevich.movieland.entity.Country;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class JdbcCountryDao implements ICountryDao {

    private final Logger LOG = LoggerFactory.getLogger(getClass());
    private static final CountryRowMapper COUNTRY_ROW_MAPPER = new CountryRowMapper();

    @Autowired
    private NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    @Autowired
    private String getCountriesByMovieIdSQL;

    @Override
    public List<Country> getByMovieId(int movieId) {
        MapSqlParameterSource parameters = new MapSqlParameterSource();
        parameters.addValue("movieId", movieId);

        LOG.info("Start query to get countries from DB by movieId");
        long startTime = System.currentTimeMillis();
        List<Country> countries = namedParameterJdbcTemplate.query(getCountriesByMovieIdSQL, parameters, COUNTRY_ROW_MAPPER);
        LOG.info("Finish query to get countries from DB by movieId. It took {} ms", System.currentTimeMillis() - startTime);
        return countries;
    }
}
