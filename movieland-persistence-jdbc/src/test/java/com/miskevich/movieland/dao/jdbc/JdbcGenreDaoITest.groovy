package com.miskevich.movieland.dao.jdbc

import com.miskevich.movieland.dao.jdbc.provider.SQLDataProvider
import com.miskevich.movieland.entity.Genre
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.dao.DuplicateKeyException
import org.springframework.test.context.ContextConfiguration
import org.springframework.test.context.testng.AbstractTestNGSpringContextTests
import org.testng.annotations.Test

import static org.testng.Assert.assertNotNull

@ContextConfiguration(locations = "classpath:spring/jdbc-context.xml")
class JdbcGenreDaoITest extends AbstractTestNGSpringContextTests {

    @Autowired
    private JdbcGenreDao jdbcGenreDao

    @Test
    void testGetAll() {
        def genres = jdbcGenreDao.getAll()
        for (Genre genre : genres) {
            assertNotNull(genre.getName())
        }
    }

    @Test
    void testGetByMovieId() {
        def genres = jdbcGenreDao.getByMovieId(1)
        for (Genre genre : genres) {
            assertNotNull(genre.getName())
        }
    }

    @Test(dataProvider = 'provideMovieForEnrichmentSave', dataProviderClass = SQLDataProvider.class,
            expectedExceptionsMessageRegExp = '.*Duplicate entry \'1-1\' for key \'unique_index\'', expectedExceptions = DuplicateKeyException.class)
    void testSaveMovieGenresDuplicateKey(movie) {
        jdbcGenreDao.persist(movie)
    }

    @Test(dataProvider = 'provideMovieForEnrichmentUpdate', dataProviderClass = SQLDataProvider.class,
            expectedExceptionsMessageRegExp = '.*Duplicate entry \'1-1\' for key \'unique_index\'', expectedExceptions = DuplicateKeyException.class)
    void testUpdateMovieGenresDuplicateKey(movie) {
        jdbcGenreDao.update(movie)
    }
}
