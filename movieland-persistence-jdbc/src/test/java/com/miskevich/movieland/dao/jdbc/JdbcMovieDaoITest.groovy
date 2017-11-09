package com.miskevich.movieland.dao.jdbc

import com.miskevich.movieland.dao.jdbc.provider.SQLDataProvider
import com.miskevich.movieland.entity.Movie
import com.miskevich.movieland.model.SortingField
import com.miskevich.movieland.model.SortingType
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.test.context.ContextConfiguration
import org.springframework.test.context.testng.AbstractTestNGSpringContextTests
import org.testng.annotations.Test

import static org.testng.Assert.assertNotNull

@ContextConfiguration(locations = "classpath:spring/jdbc-context.xml")
class JdbcMovieDaoITest extends AbstractTestNGSpringContextTests {

    @Autowired
    private JdbcMovieDao jdbcMovieDao

    @Test
    void testGetAll() {
        def movies = jdbcMovieDao.getAll(new LinkedHashMap<>())
        for (Movie movie : movies) {
            assertNotNull(movie.getNameRussian())
            assertNotNull(movie.getNameNative())
            assertNotNull(movie.getYearOfRelease())
            assertNotNull(movie.getDescription())
            assertNotNull(movie.getPicturePath())
        }
    }

    @Test(dataProvider = "provideParamsSQL", dataProviderClass = SQLDataProvider.class)
    void testGetAllWithSorting(Map<SortingField, SortingType> paramsMap) {
        def movies = jdbcMovieDao.getAll(paramsMap)
        for (Movie movie : movies) {
            assertNotNull(movie.getNameRussian())
            assertNotNull(movie.getNameNative())
            assertNotNull(movie.getYearOfRelease())
            assertNotNull(movie.getDescription())
            assertNotNull(movie.getPicturePath())
        }
    }

    @Test
    void testGetThreeRandomMovies() {
        def movies = jdbcMovieDao.getThreeRandomMovies()
        for (Movie movie : movies) {
            assertNotNull(movie.getNameRussian())
            assertNotNull(movie.getNameNative())
            assertNotNull(movie.getYearOfRelease())
            assertNotNull(movie.getDescription())
            assertNotNull(movie.getPicturePath())
        }
    }

    @Test
    void testGetByGenre() {
        def movies = jdbcMovieDao.getByGenre(3, new LinkedHashMap<SortingField, SortingType>())
        for (Movie movie : movies) {
            assertNotNull(movie.getNameRussian())
            assertNotNull(movie.getNameNative())
            assertNotNull(movie.getYearOfRelease())
            assertNotNull(movie.getDescription())
            assertNotNull(movie.getPicturePath())
        }
    }

    @Test(dataProvider = "provideParamsSQL", dataProviderClass = SQLDataProvider.class)
    void testGetByGenreWithSorting(Map<SortingField, SortingType> paramsMap) {
        def movies = jdbcMovieDao.getByGenre(3, paramsMap)
        for (Movie movie : movies) {
            assertNotNull(movie.getNameRussian())
            assertNotNull(movie.getNameNative())
            assertNotNull(movie.getYearOfRelease())
            assertNotNull(movie.getDescription())
            assertNotNull(movie.getPicturePath())
        }
    }

    @Test
    void testGetById() {
        def movie = jdbcMovieDao.getById(3)
        assertNotNull(movie.getNameRussian())
        assertNotNull(movie.getNameNative())
        assertNotNull(movie.getYearOfRelease())
        assertNotNull(movie.getDescription())
        assertNotNull(movie.getPicturePath())
    }
}
