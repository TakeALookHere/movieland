package com.miskevich.movieland.dao.jdbc;

import com.miskevich.movieland.dao.ICountryDao;
import com.miskevich.movieland.dao.jdbc.mapper.CountryRowMapper;
import com.miskevich.movieland.entity.Country;
import com.miskevich.movieland.entity.Movie;
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
    @Autowired
    private String getAllCountriesSQL;
    @Autowired
    private String addMovieCountriesSQL;
    @Autowired
    private String updateMovieCountriesSQL;

    @Override
    public List<Country> getByMovieId(int movieId) {
        MapSqlParameterSource parameters = new MapSqlParameterSource("movieId", movieId);

        LOG.info("Start query to get countries from DB by movieId");
        long startTime = System.currentTimeMillis();
        List<Country> countries = namedParameterJdbcTemplate.query(getCountriesByMovieIdSQL, parameters, COUNTRY_ROW_MAPPER);
        LOG.info("Finish query to get countries from DB by movieId. It took {} ms", System.currentTimeMillis() - startTime);
        return countries;
    }

    @Override
    public List<Country> getAll() {
        LOG.info("Start query to get all countries from DB");
        long startTime = System.currentTimeMillis();
        List<Country> countries = namedParameterJdbcTemplate.query(getAllCountriesSQL, COUNTRY_ROW_MAPPER);
        LOG.info("Finish query to get all countries from DB. It took {} ms", System.currentTimeMillis() - startTime);
        return countries;
    }

    @Override
    public void persist(Movie movie) {
        for (int i = 0; i < movie.getCountries().size(); i++) {
            int movieId = movie.getId();
            int countryId = movie.getCountries().get(i).getId();
            MapSqlParameterSource parameters = populateSQLParameters(movieId, countryId);

            LOG.info("Start query to insert countryId {} for movieId {}", countryId, movieId);
            long startTime = System.currentTimeMillis();
            namedParameterJdbcTemplate.update(addMovieCountriesSQL, parameters);
            LOG.info("Finish query to insert countryId {} for movieId {}. It took {} ms", countryId, movieId, System.currentTimeMillis() - startTime);
        }
    }

    private MapSqlParameterSource populateSQLParameters(int movieId, int countryId) {
        MapSqlParameterSource parameters = new MapSqlParameterSource();
        parameters.addValue("movieId", movieId);
        parameters.addValue("countryId", countryId);
        return parameters;
    }

    @Override
    public void update(Movie movie) {
        for (int i = 0; i < movie.getCountries().size(); i++) {
            int movieId = movie.getId();
            int countryId = movie.getCountries().get(i).getId();
            MapSqlParameterSource parameters = populateSQLParameters(movieId, countryId);

            LOG.info("Start query to update countryId {} for movieId {}", countryId, movieId);
            long startTime = System.currentTimeMillis();
            namedParameterJdbcTemplate.update(updateMovieCountriesSQL, parameters);
            LOG.info("Finish query to update countryId {} for movieId {}. It took {} ms", countryId, movieId, System.currentTimeMillis() - startTime);
        }
    }
}
