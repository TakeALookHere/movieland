package com.miskevich.movieland.dao.jdbc

import com.miskevich.movieland.entity.Movie
import org.springframework.beans.factory.annotation.Autowired
import org.testng.annotations.Test

import static org.testng.Assert.assertNotNull

class JdbcMovieDaoTest {

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
}
