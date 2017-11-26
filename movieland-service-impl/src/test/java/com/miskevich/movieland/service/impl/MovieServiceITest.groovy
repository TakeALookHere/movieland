package com.miskevich.movieland.service.impl

import com.miskevich.movieland.service.IMovieService
import com.miskevich.movieland.service.provider.ServiceDataProvider
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.dao.DuplicateKeyException
import org.springframework.test.context.ContextConfiguration
import org.springframework.test.context.testng.AbstractTestNGSpringContextTests
import org.testng.annotations.Test

import static org.testng.Assert.assertNotEquals

@ContextConfiguration(locations = "classpath:spring/service-context.xml")
class MovieServiceITest extends AbstractTestNGSpringContextTests {

    @Autowired
    private IMovieService movieService

    @Test(dataProvider = 'provideMovieForEnrichmentSave', dataProviderClass = ServiceDataProvider.class)
    void testSaveSuccess(movie) {
        movieService.save(movie)
    }

    @Test(dataProvider = 'provideMovieForUpdateUniqueConstraint', dataProviderClass = ServiceDataProvider.class,
            expectedExceptionsMessageRegExp = '.*Duplicate entry \'1-1\' for key \'unique_index\'.*', expectedExceptions = DuplicateKeyException.class)
    void testUpdateUniqueConstraint(movie) {
        movieService.update(movie)
    }

    @Test
    void validate() {
        def movie = movieService.getById(1)
        println movie.rating
        assertNotEquals(movie.rating, 0d)
    }
}
