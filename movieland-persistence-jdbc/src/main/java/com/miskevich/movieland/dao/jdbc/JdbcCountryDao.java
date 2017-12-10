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

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

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
    private String removeCountriesSQL;

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
        List<Country> countries = movie.getCountries();
        if (countries != null) {
            int movieId = movie.getId();

            List<Map<String, Object>> batchValues = new ArrayList<>(countries.size());
            for (Country country : countries) {
                batchValues.add(
                        new MapSqlParameterSource("movieId", movieId)
                                .addValue("countryId", country.getId())
                                .getValues());
            }

            LOG.info("Start query to insert countries for movieId {}", movieId);
            long startTime = System.currentTimeMillis();
            //How to deal with this unchecked assignment?
            namedParameterJdbcTemplate.batchUpdate(addMovieCountriesSQL, batchValues.toArray(new Map[countries.size()]));
            LOG.info("Finish query to insert countries for movieId {}. It took {} ms", movieId, System.currentTimeMillis() - startTime);
        }
    }

    @Override
    public void remove(Movie movie) {
        if (movie.getCountries() != null) {
            int movieId = movie.getId();
            MapSqlParameterSource parameters = new MapSqlParameterSource("movieId", movieId);
            LOG.info("Start query to remove countries for movieId {}", movieId);
            long startTime = System.currentTimeMillis();
            namedParameterJdbcTemplate.update(removeCountriesSQL, parameters);
            LOG.info("Finish query to remove countries for movieId {}. It took {} ms", movieId, System.currentTimeMillis() - startTime);
        }
    }
}
