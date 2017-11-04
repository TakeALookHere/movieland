package com.miskevich.movieland.dao.jdbc.mapper

import com.miskevich.movieland.dao.jdbc.provider.DataProviderMapper
import org.testng.annotations.Test

import java.sql.ResultSet

import static org.mockito.Mockito.mock
import static org.mockito.Mockito.when
import static org.testng.Assert.assertEquals

class CountryRowMapperTest {

    private CountryRowMapper countryRowMapper = new CountryRowMapper()

    @Test(dataProvider = 'provideCountry', dataProviderClass = DataProviderMapper.class)
    void testMapRow(def expectedCountry) {
        ResultSet resultSet = mock(ResultSet.class)
        when(resultSet.next()).thenReturn(true).thenReturn(false)
        when(resultSet.getInt("id")).thenReturn(1)
        when(resultSet.getString("name")).thenReturn('США')

        def actualCountry = countryRowMapper.mapRow(resultSet, 0)
        assertEquals(actualCountry, expectedCountry)
    }
}
