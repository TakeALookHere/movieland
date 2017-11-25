package com.miskevich.movieland.service.impl

import com.miskevich.movieland.dao.IMovieDao
import com.miskevich.movieland.entity.Movie
import com.miskevich.movieland.service.IGenreService
import com.miskevich.movieland.service.IMovieService
import com.miskevich.movieland.service.provider.ServiceDataProvider
import org.mockito.Mock
import org.mockito.MockitoAnnotations
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.test.context.ContextConfiguration
import org.springframework.test.context.testng.AbstractTestNGSpringContextTests
import org.testng.annotations.BeforeTest
import org.testng.annotations.Test

import java.sql.SQLIntegrityConstraintViolationException

import static org.mockito.Matchers.any
import static org.mockito.Mockito.when

@ContextConfiguration(locations = "classpath:spring/service-context.xml")
class MovieServiceITest extends AbstractTestNGSpringContextTests {

    @Mock
    private IMovieDao movieDao
    @Autowired
    private IMovieService movieService
    @Mock
    private IGenreService genreService

    @BeforeTest
    private void setup() {
        MockitoAnnotations.initMocks(this)
    }

    @Test(dataProvider = 'provideMovieForEnrichmentSave', dataProviderClass = ServiceDataProvider.class)
    void testSaveSuccess(movie) {
        movieService.save(movie)
    }

    @Test(dataProvider = 'provideMovieForEnrichmentSaveUniqueConstraint', dataProviderClass = ServiceDataProvider.class)
    void testSaveUniqueConstraint(movie) {
        when(movieDao.save(any(Movie.class))).thenReturn(movie)
        when(genreService.persist(any(Movie.class))).thenThrow(SQLIntegrityConstraintViolationException.class)
        println movie.id
        def savedMovie = movieService.save(movie)
        println savedMovie
    }
}
