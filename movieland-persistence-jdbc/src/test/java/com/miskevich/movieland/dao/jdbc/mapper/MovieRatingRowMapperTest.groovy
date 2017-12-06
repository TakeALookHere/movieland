package com.miskevich.movieland.dao.jdbc.mapper

import com.miskevich.movieland.dao.jdbc.provider.MapperDataProvider
import com.miskevich.movieland.entity.Movie
import com.miskevich.movieland.model.MovieRating
import org.testng.annotations.Test

import java.sql.ResultSet

import static org.mockito.Mockito.mock
import static org.mockito.Mockito.when
import static org.testng.Assert.assertEquals

class MovieRatingRowMapperTest {
    private MovieRatingRowMapper movieRatingRowMapper = new MovieRatingRowMapper()

    @Test(dataProvider = "movieRatingVotes", dataProviderClass = MapperDataProvider.class)
    void testMapRow(MovieRating expectedMovie) {
        ResultSet resultSet = mock(ResultSet.class)
        when(resultSet.next()).thenReturn(true).thenReturn(false)
        when(resultSet.getInt("movie_id")).thenReturn(expectedMovie.movie.id)
        when(resultSet.getDouble("rating")).thenReturn(expectedMovie.rating)
        when(resultSet.getLong("votes")).thenReturn(expectedMovie.votes)

        MovieRating actualMovie = movieRatingRowMapper.mapRow(resultSet, 0)

        assertEquals(actualMovie.movie.id, expectedMovie.movie.id)
        assertEquals(actualMovie.rating, expectedMovie.rating)
        assertEquals(actualMovie.votes, expectedMovie.votes)
    }
}
