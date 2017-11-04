package com.miskevich.movieland.web.controller

import com.miskevich.movieland.web.controller.provider.DataProviderController
import com.miskevich.movieland.web.dto.MovieDto
import org.testng.annotations.Test

import static org.testng.Assert.assertEquals

class MovieUtilTest {

    MovieController movieController = new MovieController()

    @Test(dataProvider = 'provideMovieForRateConversion', dataProviderClass = DataProviderController.class)
    void testEnrichMovieWithPriceForRate(MovieDto movieBeforeConversion, MovieDto expectedMovieAfterConversion) {
        double rate = 26.931378
        def actualMovieAfterConversion = movieController.enrichMovieWithPriceForRate(movieBeforeConversion, rate)
        assertEquals(actualMovieAfterConversion, expectedMovieAfterConversion)
    }
}
