package com.miskevich.movieland.dao.jdbc

import com.miskevich.movieland.dao.jdbc.provider.SQLDataProvider
import com.miskevich.movieland.entity.Country
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.dao.DuplicateKeyException
import org.springframework.test.context.ContextConfiguration
import org.springframework.test.context.testng.AbstractTestNGSpringContextTests
import org.testng.annotations.Test

import static org.testng.Assert.assertNotNull

@ContextConfiguration(locations = "classpath:spring/jdbc-context.xml")
class JdbcCountryDaoITest extends AbstractTestNGSpringContextTests {

    @Autowired
    private JdbcCountryDao jdbcCountryDao

    @Test
    void testGetByMovieId() {
        def countries = jdbcCountryDao.getByMovieId(6)
        for (Country country : countries) {
            assertNotNull(country.getName())
        }
    }

    @Test
    void testGetAllCountries() {
        def countries = jdbcCountryDao.getAll()
        for (Country country : countries) {
            assertNotNull(country.getName())
        }
    }

    @Test(dataProvider = 'provideMovieForEnrichmentSave', dataProviderClass = SQLDataProvider.class,
            expectedExceptionsMessageRegExp = '.*Duplicate entry \'1-1\' for key \'unique_index\'', expectedExceptions = DuplicateKeyException.class)
    void testSaveMovieGenresDuplicateKey(movie) {
        jdbcCountryDao.persist(movie)
    }

    @Test(dataProvider = 'provideMovieForEnrichmentUpdate', dataProviderClass = SQLDataProvider.class,
            expectedExceptionsMessageRegExp = '.*Duplicate entry \'1-1\' for key \'unique_index\'', expectedExceptions = DuplicateKeyException.class)
    void testUpdateMovieGenresDuplicateKey(movie) {
        jdbcCountryDao.update(movie)
    }
}
