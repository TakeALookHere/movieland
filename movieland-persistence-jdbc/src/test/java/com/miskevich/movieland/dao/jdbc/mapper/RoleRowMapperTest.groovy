package com.miskevich.movieland.dao.jdbc.mapper

import com.miskevich.movieland.model.Role
import org.testng.annotations.Test

import java.sql.ResultSet

import static org.mockito.Mockito.mock
import static org.mockito.Mockito.when
import static org.testng.Assert.assertEquals

class RoleRowMapperTest {

    private RoleRowMapper roleRowMapper = new RoleRowMapper()

    @Test
    void testMapRow() {
        ResultSet resultSet = mock(ResultSet.class)
        when(resultSet.next()).thenReturn(true).thenReturn(false)
        when(resultSet.getString("name")).thenReturn('USER')
        def actualRole = roleRowMapper.mapRow(resultSet, 0)
        assertEquals(actualRole, Role.USER)
    }
}
