package com.miskevich.movieland.dao.jdbc

import com.miskevich.movieland.dao.jdbc.provider.SQLDataProvider
import com.miskevich.movieland.entity.Genre
import com.miskevich.movieland.entity.Movie
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.dao.DuplicateKeyException
import org.springframework.test.context.ContextConfiguration
import org.springframework.test.context.testng.AbstractTestNGSpringContextTests
import org.testng.annotations.Test

import static org.testng.Assert.assertEquals
import static org.testng.Assert.assertNotNull

@ContextConfiguration(locations = "classpath:spring/jdbc-context.xml")
class JdbcGenreDaoITest extends AbstractTestNGSpringContextTests {

    @Autowired
    private JdbcGenreDao jdbcGenreDao
    @Autowired
    private JdbcMovieDao jdbcMovieDao

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

    @Test(dataProvider = 'provideMovieForEnrichmentSaveUniqueConstraint', dataProviderClass = SQLDataProvider.class,
            expectedExceptionsMessageRegExp = '.*Duplicate entry \'1-1\' for key \'unique_index\'', expectedExceptions = DuplicateKeyException.class)
    void testSaveMovieGenresDuplicateKey(movie) {
        jdbcGenreDao.persist(movie)
    }

    @Test(dataProvider = 'provideMovieSave', dataProviderClass = SQLDataProvider.class)
    void testRemove(expectedMovie) {
        def movie = jdbcMovieDao.persist(expectedMovie)
        jdbcGenreDao.remove(movie)
        assertEquals(jdbcGenreDao.getByMovieId(movie.id).size(), 0)
    }

    @Test(dataProvider = 'provideMoviePersist', dataProviderClass = SQLDataProvider.class)
    void testPersist(Movie expectedMovie){
        jdbcGenreDao.remove(expectedMovie)
        jdbcGenreDao.persist(expectedMovie)
        def actualGenres = jdbcGenreDao.getByMovieId(expectedMovie.id)
        assertEquals(actualGenres[0].id, expectedMovie.genres[0].id)
        assertEquals(actualGenres[1].id, expectedMovie.genres[1].id)
    }
}
