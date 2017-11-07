package com.miskevich.movieland.dao.jdbc.mapper

import com.miskevich.movieland.dao.jdbc.provider.MapperDataProvider
import com.miskevich.movieland.entity.User
import org.testng.annotations.Test

import java.sql.ResultSet

import static org.mockito.Mockito.mock
import static org.mockito.Mockito.when
import static org.testng.Assert.assertEquals

class UserRowMapperTest {

    private UserRowMapper userRowMapper = new UserRowMapper()

    @Test(dataProvider = 'provideUser', dataProviderClass = MapperDataProvider.class)
    void testMapRow(User expectedUser) {
        ResultSet resultSet = mock(ResultSet.class)
        when(resultSet.next()).thenReturn(true).thenReturn(false)
        when(resultSet.getInt("id")).thenReturn(expectedUser.getId())
        when(resultSet.getString("nickname")).thenReturn(expectedUser.getNickname())
        when(resultSet.getString("email")).thenReturn(expectedUser.getEmail())
        when(resultSet.getString("password")).thenReturn(expectedUser.getPassword())

        def actualUser = userRowMapper.mapRow(resultSet, 0)
        assertEquals(actualUser.getId(), expectedUser.getId())
        assertEquals(actualUser.getNickname(), expectedUser.getNickname())
        assertEquals(actualUser.getEmail(), expectedUser.getEmail())
        assertEquals(actualUser.getPassword(), expectedUser.getPassword())
    }
}
