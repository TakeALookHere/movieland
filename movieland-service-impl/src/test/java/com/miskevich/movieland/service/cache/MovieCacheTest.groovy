package com.miskevich.movieland.service.cache

import com.miskevich.movieland.entity.Movie
import com.miskevich.movieland.service.provider.ServiceDataProvider
import org.testng.annotations.Test

import static org.testng.Assert.assertEquals

class MovieCacheTest {

    @Test(dataProvider = 'movieCopy', dataProviderClass = ServiceDataProvider.class)
    void testCreateMovieCopy(Movie movieExpected){
        Movie movieCopy = new Movie(movieExpected)
        assertEquals(movieCopy.id, movieExpected.id)
        assertEquals(movieCopy.nameRussian, movieExpected.nameRussian)
        assertEquals(movieCopy.nameNative, movieExpected.nameNative)
        assertEquals(movieCopy.yearOfRelease, movieExpected.yearOfRelease)
        assertEquals(movieCopy.countries, movieExpected.countries)
        assertEquals(movieCopy.description, movieExpected.description)
        assertEquals(movieCopy.rating, movieExpected.rating)
        assertEquals(movieCopy.price, movieExpected.price)
        assertEquals(movieCopy.picturePath, movieExpected.picturePath)
        assertEquals(movieCopy.genres[0].id, movieExpected.genres[0].id)
        assertEquals(movieCopy.genres[1].id, movieExpected.genres[1].id)
        assertEquals(movieCopy.reviews, movieExpected.reviews)
    }
}
