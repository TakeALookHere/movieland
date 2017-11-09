package com.miskevich.movieland.dao.jdbc

import com.miskevich.movieland.dao.jdbc.provider.SQLDataProvider
import com.miskevich.movieland.model.SortingField
import com.miskevich.movieland.model.SortingType
import org.testng.annotations.Test

import static org.testng.Assert.assertEquals

class JdbcMovieDaoTest {

    JdbcMovieDao movieDao = new JdbcMovieDao()

    @Test(dataProvider = "provideParamsMapWithSQL", dataProviderClass = SQLDataProvider.class)
    void testGenerateSortingSQL(Map<SortingField, SortingType> paramsMap, def expectedSQL) {
        def actualSortingSQL = movieDao.generateSortingSQL(paramsMap)
        assertEquals(actualSortingSQL, expectedSQL)
    }
}
