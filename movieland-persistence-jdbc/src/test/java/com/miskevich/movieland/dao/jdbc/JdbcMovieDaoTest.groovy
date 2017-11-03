package com.miskevich.movieland.dao.jdbc

import com.miskevich.movieland.dao.jdbc.provider.DataProviderSQL
import org.testng.annotations.Test

import static org.testng.Assert.assertEquals

class JdbcMovieDaoTest {

    JdbcMovieDao movieDao = new JdbcMovieDao()

    @Test(dataProvider = "provideParamsMapWithSQL", dataProviderClass = DataProviderSQL.class)
    void testGenerateSortingSQL(Map<String, String> paramsMap, def expectedSQL) {
        def actualSortingSQL = movieDao.generateSortingSQL(paramsMap)
        assertEquals(actualSortingSQL, expectedSQL)
    }
}
