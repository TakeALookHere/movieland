package com.miskevich.movieland.dao.jdbc.mapper

import com.miskevich.movieland.dao.jdbc.mapper.provider.DataProviderMapper
import com.miskevich.movieland.entity.Movie
import org.testng.annotations.Test

import java.sql.Date
import java.sql.ResultSet

import static org.mockito.Mockito.mock
import static org.mockito.Mockito.when
import static org.testng.Assert.assertEquals

class MovieRowMapperTest {

    private MovieRowMapper movieRowMapper = new MovieRowMapper()

    @Test(dataProvider = "provideMovie", dataProviderClass = DataProviderMapper.class)
    void testMapRow(Movie expectedMovie) {
        ResultSet resultSet = mock(ResultSet.class)
        when(resultSet.next()).thenReturn(true).thenReturn(false)
        when(resultSet.getInt("id")).thenReturn(1)
        when(resultSet.getString("name_russian")).thenReturn("Побег из Шоушенка")
        when(resultSet.getString("name_native")).thenReturn("The Shawshank Redemption")
        when(resultSet.getDate("released_date")).thenReturn(Date.valueOf("2016-01-15"))
        when(resultSet.getString("plot")).thenReturn("Успешный банкир Энди Дюфрейн обвинен в убийстве собственной жены и ее любовника. Оказавшись в тюрьме под названием Шоушенк, он сталкивается с жестокостью и беззаконием, царящими по обе стороны решетки. Каждый, кто попадает в эти стены, становится их рабом до конца жизни. Но Энди, вооруженный живым умом и доброй душой, отказывается мириться с приговором судьбы и начинает разрабатывать невероятно дерзкий план своего освобождения.")
        when(resultSet.getDouble("rating")).thenReturn(8.9d)
        when(resultSet.getDouble("price")).thenReturn(123.45d)
        when(resultSet.getString("picture_path")).thenReturn("shawshank173_173.jpg")

        Movie actualMovie = movieRowMapper.mapRow(resultSet, 0)
        assertEquals(actualMovie, expectedMovie)
    }
}
