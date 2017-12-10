package com.miskevich.movieland.dao.jdbc

import com.miskevich.movieland.dao.jdbc.provider.SQLDataProvider
import com.miskevich.movieland.entity.Country
import com.miskevich.movieland.entity.Movie
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.dao.DuplicateKeyException
import org.springframework.test.context.ContextConfiguration
import org.springframework.test.context.testng.AbstractTestNGSpringContextTests
import org.testng.annotations.Test

import static org.testng.Assert.assertEquals
import static org.testng.Assert.assertNotNull

@ContextConfiguration(locations = "classpath:spring/jdbc-context.xml")
class JdbcCountryDaoITest extends AbstractTestNGSpringContextTests {

    @Autowired
    private JdbcCountryDao jdbcCountryDao
    @Autowired
    private JdbcMovieDao jdbcMovieDao

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

    @Test(dataProvider = 'provideMovieForEnrichmentSaveUniqueConstraint', dataProviderClass = SQLDataProvider.class,
            expectedExceptionsMessageRegExp = '.*Duplicate entry \'1-1\' for key \'unique_index\'', expectedExceptions = DuplicateKeyException.class)
    void testSaveMovieGenresDuplicateKey(movie) {
        jdbcCountryDao.persist(movie)
    }

    @Test(dataProvider = 'provideMovieSave', dataProviderClass = SQLDataProvider.class)
    void testRemove(expectedMovie) {
        def movie = jdbcMovieDao.persist(expectedMovie)
        jdbcCountryDao.remove(movie)
        assertEquals(jdbcCountryDao.getByMovieId(movie.id).size(), 0)
    }

    @Test(dataProvider = 'provideMoviePersist', dataProviderClass = SQLDataProvider.class)
    void testPersist(Movie expectedMovie){
        jdbcCountryDao.remove(expectedMovie)
        jdbcCountryDao.persist(expectedMovie)
        def actualCountries = jdbcCountryDao.getByMovieId(expectedMovie.id)
        assertEquals(actualCountries[0].id, expectedMovie.countries[0].id)
        assertEquals(actualCountries[1].id, expectedMovie.countries[1].id)
    }
}
