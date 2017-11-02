package com.miskevich.movieland.dao.jdbc


import com.miskevich.movieland.entity.Movie
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
        def movies = jdbcMovieDao.getAll()
        for (Movie movie : movies) {
            assertNotNull(movie.getId())
            assertNotNull(movie.getNameRussian())
            assertNotNull(movie.getNameNative())
            assertNotNull(movie.getReleasedDate())
            assertNotNull(movie.getPlot())
            assertNotNull(movie.getRating())
            assertNotNull(movie.getPrice())
            assertNotNull(movie.getPicturePath())
        }
    }

    @Test
    void testGetAllRatingDesc() {
        def movies = jdbcMovieDao.getAllRatingDesc()
        for (Movie movie : movies) {
            assertNotNull(movie.getId())
            assertNotNull(movie.getNameRussian())
            assertNotNull(movie.getNameNative())
            assertNotNull(movie.getReleasedDate())
            assertNotNull(movie.getPlot())
            assertNotNull(movie.getRating())
            assertNotNull(movie.getPrice())
            assertNotNull(movie.getPicturePath())
        }
    }

    @Test
    void testGetAllPriceAcs() {
        def movies = jdbcMovieDao.getAllPriceAsc()
        for (Movie movie : movies) {
            assertNotNull(movie.getId())
            assertNotNull(movie.getNameRussian())
            assertNotNull(movie.getNameNative())
            assertNotNull(movie.getReleasedDate())
            assertNotNull(movie.getPlot())
            assertNotNull(movie.getRating())
            assertNotNull(movie.getPrice())
            assertNotNull(movie.getPicturePath())
        }
    }

    @Test
    void testGetAllPriceDesc() {
        def movies = jdbcMovieDao.getAllPriceDesc()
        for (Movie movie : movies) {
            assertNotNull(movie.getId())
            assertNotNull(movie.getNameRussian())
            assertNotNull(movie.getNameNative())
            assertNotNull(movie.getReleasedDate())
            assertNotNull(movie.getPlot())
            assertNotNull(movie.getRating())
            assertNotNull(movie.getPrice())
            assertNotNull(movie.getPicturePath())
        }
    }

    @Test
    void testGetThreeRandomMovies(){
        def movies = jdbcMovieDao.getThreeRandomMovies()
        for (Movie movie : movies) {
            assertNotNull(movie.getId())
            assertNotNull(movie.getNameRussian())
            assertNotNull(movie.getNameNative())
            assertNotNull(movie.getReleasedDate())
            assertNotNull(movie.getPlot())
            assertNotNull(movie.getRating())
            assertNotNull(movie.getPrice())
            assertNotNull(movie.getPicturePath())
        }
    }

    @Test
    void testGetByGenre(){
        def movies = jdbcMovieDao.getByGenre(3)
        for (Movie movie : movies) {
            assertNotNull(movie.getId())
            assertNotNull(movie.getNameRussian())
            assertNotNull(movie.getNameNative())
            assertNotNull(movie.getReleasedDate())
            assertNotNull(movie.getPlot())
            assertNotNull(movie.getRating())
            assertNotNull(movie.getPrice())
            assertNotNull(movie.getPicturePath())
        }
    }
}
