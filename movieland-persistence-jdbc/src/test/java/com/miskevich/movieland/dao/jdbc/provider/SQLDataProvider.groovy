package com.miskevich.movieland.dao.jdbc.provider

import com.miskevich.movieland.entity.Movie
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

    @DataProvider(name = "provideMovieSave")
    static Object[][] provideMovieSave() {

        def movieExpected = new Movie(nameRussian: "Зеленая миля", nameNative: "The Green Mile", yearOfRelease: convertStringToDate("1999-01-01"),
                description: "Обвиненный в страшном преступлении, Джон Коффи оказывается в блоке смертников тюрьмы «Холодная гора». Вновь прибывший обладал поразительным ростом и был пугающе спокоен, что, впрочем, никак не влияло на отношение к нему начальника блока Пола Эджкомба, привыкшего исполнять приговор.",
                rating: 8.9, price: 134.67, picturePath: "green_mile173_173.jpg")

        def array = new Object[1][]
        array[0] = [movieExpected]
        return array
    }
}
