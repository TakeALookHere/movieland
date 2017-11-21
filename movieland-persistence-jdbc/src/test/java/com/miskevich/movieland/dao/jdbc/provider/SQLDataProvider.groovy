package com.miskevich.movieland.dao.jdbc.provider

import com.miskevich.movieland.entity.Country
import com.miskevich.movieland.entity.Genre
import com.miskevich.movieland.entity.Movie
import com.miskevich.movieland.entity.Review
import com.miskevich.movieland.entity.User
import com.miskevich.movieland.model.SortingField
import com.miskevich.movieland.model.SortingType
import org.testng.annotations.DataProvider

import java.time.LocalDate
import java.time.format.DateTimeFormatter

class SQLDataProvider {

    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd", Locale.ENGLISH)

    private static LocalDate convertStringToDate(String releasedDate) {
        return LocalDate.parse(releasedDate, DATE_FORMATTER)
    }

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
                rating: 8.9, price: 134.67, picturePath: "green_mile173_173.jpg",
                genres: [new Genre(1, 'драма'), new Genre(2, 'криминал')],
                countries: [new Country(id: 1, name: 'США'), new Country(id: 2, name: 'Франция')],
                reviews: [new Review(id: 1, text: 'Гениальное кино! Смотришь и думаешь «Так не бывает!», но позже понимаешь, что только так и должно быть. Начинаешь заново осмысливать значение фразы, которую постоянно используешь в своей жизни, «Надежда умирает последней». Ведь если ты не надеешься, то все в твоей жизни гаснет, не остается смысла. Фильм наполнен бесконечным числом правильных афоризмов. Я уверена, что буду пересматривать его сотни раз.',
                        movie: new Movie(id: 1),
                        user: new User(id: 1)),
                          new Review(id: 2, text: 'Назначается Киношедевром среди развлекательных фильмов.',
                                  movie: new Movie(id: 1),
                                  user: new User(id: 5))])

        def array = new Object[1][]
        array[0] = [movieExpected]
        return array
    }

    @DataProvider(name = "provideMovieForEnrichmentSave")
    static Object[][] provideMovieForEnrichmentSave() {

        def movieExpected = new Movie(id: 1, nameRussian: "Зеленая миля", nameNative: "The Green Mile", yearOfRelease: convertStringToDate("1999-01-01"),
                description: "Обвиненный в страшном преступлении, Джон Коффи оказывается в блоке смертников тюрьмы «Холодная гора». Вновь прибывший обладал поразительным ростом и был пугающе спокоен, что, впрочем, никак не влияло на отношение к нему начальника блока Пола Эджкомба, привыкшего исполнять приговор.",
                rating: 8.9, price: 134.67, picturePath: "green_mile173_173.jpg",
                genres: [new Genre(1, 'драма'), new Genre(2, 'криминал')],
                countries: [new Country(id: 1, name: 'США'), new Country(id: 2, name: 'Франция')],
                reviews: [new Review(id: 1, text: 'Гениальное кино! Смотришь и думаешь «Так не бывает!», но позже понимаешь, что только так и должно быть. Начинаешь заново осмысливать значение фразы, которую постоянно используешь в своей жизни, «Надежда умирает последней». Ведь если ты не надеешься, то все в твоей жизни гаснет, не остается смысла. Фильм наполнен бесконечным числом правильных афоризмов. Я уверена, что буду пересматривать его сотни раз.',
                        movie: new Movie(id: 1),
                        user: new User(id: 1)),
                          new Review(id: 2, text: 'Назначается Киношедевром среди развлекательных фильмов.',
                                  movie: new Movie(id: 1),
                                  user: new User(id: 5))])

        def array = new Object[1][]
        array[0] = [movieExpected]
        return array
    }
}
