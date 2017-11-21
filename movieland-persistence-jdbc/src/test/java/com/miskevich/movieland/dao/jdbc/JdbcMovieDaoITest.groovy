package com.miskevich.movieland.dao.jdbc

import com.miskevich.movieland.dao.jdbc.provider.SQLDataProvider
import com.miskevich.movieland.entity.Movie
import com.miskevich.movieland.model.SortingField
import com.miskevich.movieland.model.SortingType
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.test.context.ContextConfiguration
import org.springframework.test.context.testng.AbstractTestNGSpringContextTests
import org.testng.annotations.Test

import static org.testng.Assert.assertEquals
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

    @Test(dataProvider = 'provideMovieSave', dataProviderClass = SQLDataProvider.class)
    void testSave(movieExpected) {
        def movie = jdbcMovieDao.saveMovie(movieExpected)
        assertEquals(movie.nameRussian, movieExpected.nameRussian)
        assertEquals(movie.nameNative, movieExpected.nameNative)
        assertEquals(movie.yearOfRelease, movieExpected.yearOfRelease)
        assertEquals(movie.description, movieExpected.description)
        assertEquals(movie.picturePath, movieExpected.picturePath)
        assertEquals(movie.rating, movieExpected.rating)
        assertEquals(movie.price, movieExpected.price)
        assertEquals(movie.genres[0].id, movieExpected.genres[0].id)
        assertEquals(movie.genres[0].name, movieExpected.genres[0].name)
        assertEquals(movie.genres[1].id, movieExpected.genres[1].id)
        assertEquals(movie.genres[1].name, movieExpected.genres[1].name)
        assertEquals(movie.countries[0].id, movieExpected.countries[0].id)
        assertEquals(movie.countries[0].name, movieExpected.countries[0].name)
        assertEquals(movie.countries[1].id, movieExpected.countries[1].id)
        assertEquals(movie.countries[1].name, movieExpected.countries[1].name)
        assertEquals(movie.reviews[0].id, movieExpected.reviews[0].id)
        assertEquals(movie.reviews[0].text, movieExpected.reviews[0].text)
        assertEquals(movie.reviews[0].movie.id, movieExpected.reviews[0].movie.id)
        assertEquals(movie.reviews[0].user.id, movieExpected.reviews[0].user.id)
        assertEquals(movie.reviews[1].id, movieExpected.reviews[1].id)
        assertEquals(movie.reviews[1].text, movieExpected.reviews[1].text)
        assertEquals(movie.reviews[1].movie.id, movieExpected.reviews[1].movie.id)
        assertEquals(movie.reviews[1].user.id, movieExpected.reviews[1].user.id)
    }
}
