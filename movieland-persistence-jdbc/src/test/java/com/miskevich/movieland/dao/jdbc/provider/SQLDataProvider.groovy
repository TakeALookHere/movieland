package com.miskevich.movieland.dao.jdbc.provider

import org.testng.annotations.DataProvider

class SQLDataProvider {

    @DataProvider(name = "provideParamsMapWithSQL")
    static Object[][] provideParamsMapWithSQL() {

        def params = [
                price       : 'asc',
                name_russian: 'DESC',
                RATING      : 'desc'
        ]

        def expectedSQL = ' ORDER BY substring(price from 1 for 9) asc, IF(name_russian RLIKE \'^[a-z]\', 1, 2), name_russian DESC, substring(RATING from 1 for 9) desc'

        def array = new Object[1][]
        array[0] = [params, expectedSQL]
        return array
    }

    @DataProvider(name = "provideParamsSQL")
    static Object[][] provideParamsSQL() {

        def params = [
                price       : 'asc',
                name_russian: 'DESC',
                RATING      : 'desc'
        ]

        def array = new Object[1][]
        array[0] = [params]
        return array
    }
}
