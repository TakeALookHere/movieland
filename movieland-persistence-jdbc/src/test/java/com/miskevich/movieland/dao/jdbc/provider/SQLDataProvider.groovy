package com.miskevich.movieland.dao.jdbc.provider

import com.miskevich.movieland.model.SortingField
import com.miskevich.movieland.model.SortingType
import org.testng.annotations.DataProvider

class SQLDataProvider {

    @DataProvider(name = "provideParamsMapWithSQL")
    static Object[][] provideParamsMapWithSQL() {

        LinkedHashMap<SortingField, SortingType> params = [
                (SortingField.PRICE)       : SortingType.ASC,
                (SortingField.NAME_RUSSIAN): SortingType.DESC,
                (SortingField.RATING)      : SortingType.ASC
        ]

        def expectedSQL = ' ORDER BY substring(PRICE from 1 for 9) ASC, IF(NAME_RUSSIAN RLIKE \'^[a-z]\', 1, 2), NAME_RUSSIAN DESC, substring(RATING from 1 for 9) ASC'

        def array = new Object[1][]
        array[0] = [params, expectedSQL]
        return array
    }

    @DataProvider(name = "provideParamsSQL")
    static Object[][] provideParamsSQL() {

        LinkedHashMap<SortingField, SortingType> params = [
                (SortingField.PRICE)       : SortingType.ASC,
                (SortingField.NAME_RUSSIAN): SortingType.DESC,
                (SortingField.RATING)      : SortingType.ASC
        ]

        def array = new Object[1][]
        array[0] = [params]
        return array
    }
}
