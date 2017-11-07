package com.miskevich.movieland.web.util

import com.miskevich.movieland.web.controller.provider.ControllerDataProvider
import com.miskevich.movieland.web.dto.MovieDto
import org.testng.annotations.Test

import static org.testng.Assert.assertEquals

class RateConverterTest {

    @Test(dataProvider = 'provideMovieForRateConversion', dataProviderClass = ControllerDataProvider.class)
    void testEnrichMovieWithPriceForRate(MovieDto movieBeforeConversion, MovieDto expectedMovieAfterConversion) {
        double rate = 26.931378
        def actualMovieAfterConversion = RateConverter.enrichMovieWithPriceForRate(movieBeforeConversion, rate)
        assertEquals(actualMovieAfterConversion.getId(), expectedMovieAfterConversion.getId())
        assertEquals(actualMovieAfterConversion.getNameRussian(), expectedMovieAfterConversion.getNameRussian())
        assertEquals(actualMovieAfterConversion.getNameNative(), expectedMovieAfterConversion.getNameNative())
        assertEquals(actualMovieAfterConversion.getYearOfRelease(), expectedMovieAfterConversion.getYearOfRelease())
        assertEquals(actualMovieAfterConversion.getCountries().get(0).getId(), expectedMovieAfterConversion.getCountries().get(0).getId())
        assertEquals(actualMovieAfterConversion.getCountries().get(0).getName(), expectedMovieAfterConversion.getCountries().get(0).getName())
        assertEquals(actualMovieAfterConversion.getCountries().get(1).getId(), expectedMovieAfterConversion.getCountries().get(1).getId())
        assertEquals(actualMovieAfterConversion.getCountries().get(1).getName(), expectedMovieAfterConversion.getCountries().get(1).getName())
        assertEquals(actualMovieAfterConversion.getDescription(), expectedMovieAfterConversion.getDescription())
        assertEquals(actualMovieAfterConversion.getRating(), expectedMovieAfterConversion.getRating())
        assertEquals(actualMovieAfterConversion.getPrice(), expectedMovieAfterConversion.getPrice())
        assertEquals(actualMovieAfterConversion.getPicturePath(), expectedMovieAfterConversion.getPicturePath())
        assertEquals(actualMovieAfterConversion.getReviews().get(0).getId(), expectedMovieAfterConversion.getReviews().get(0).getId())
        assertEquals(actualMovieAfterConversion.getReviews().get(0).getText(), expectedMovieAfterConversion.getReviews().get(0).getText())
        assertEquals(actualMovieAfterConversion.getReviews().get(0).getMovie().getId(), expectedMovieAfterConversion.getReviews().get(0).getMovie().getId())
        assertEquals(actualMovieAfterConversion.getReviews().get(0).getUser().getId(), expectedMovieAfterConversion.getReviews().get(0).getUser().getId())
        assertEquals(actualMovieAfterConversion.getReviews().get(1).getId(), expectedMovieAfterConversion.getReviews().get(1).getId())
        assertEquals(actualMovieAfterConversion.getReviews().get(1).getText(), expectedMovieAfterConversion.getReviews().get(1).getText())
        assertEquals(actualMovieAfterConversion.getReviews().get(1).getMovie().getId(), expectedMovieAfterConversion.getReviews().get(1).getMovie().getId())
        assertEquals(actualMovieAfterConversion.getReviews().get(1).getUser().getId(), expectedMovieAfterConversion.getReviews().get(1).getUser().getId())
    }
}
