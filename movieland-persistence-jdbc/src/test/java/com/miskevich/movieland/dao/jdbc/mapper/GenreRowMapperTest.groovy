package com.miskevich.movieland.dao.jdbc.mapper

import com.miskevich.movieland.dao.jdbc.provider.MapperDataProvider
import org.testng.annotations.Test

import java.sql.ResultSet

import static org.mockito.Mockito.mock
import static org.mockito.Mockito.when
import static org.testng.Assert.assertEquals

class GenreRowMapperTest {

    private GenreRowMapper genreRowMapper = new GenreRowMapper()

    @Test(dataProvider = 'provideGenre', dataProviderClass = MapperDataProvider.class)
    void testMapRow(def expectedGenre) {
        ResultSet resultSet = mock(ResultSet.class)
        when(resultSet.next()).thenReturn(true).thenReturn(false)
        when(resultSet.getInt("id")).thenReturn(1)
        when(resultSet.getString("name")).thenReturn('драма')

        def actualGenre = genreRowMapper.mapRow(resultSet, 0)
        assertEquals(actualGenre, expectedGenre)
    }
}
