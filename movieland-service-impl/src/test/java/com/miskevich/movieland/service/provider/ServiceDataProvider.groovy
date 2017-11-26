package com.miskevich.movieland.service.provider

import com.miskevich.movieland.entity.*
import org.testng.annotations.DataProvider

import java.time.LocalDate
import java.time.format.DateTimeFormatter

class ServiceDataProvider {

    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd", Locale.ENGLISH)

    private static LocalDate convertStringToDate(String releasedDate) {
        return LocalDate.parse(releasedDate, DATE_FORMATTER)
    }

    @DataProvider(name = "putUserIntoCache")
    static Object[][] putUserIntoCache() {

        def user = new User(1, 'Super User', 'ronald.reynolds66@example.com')

        def array = new Object[1][]
        array[0] = [user]
        return array
    }

    @DataProvider(name = "provideMovieForEnrichmentSave")
    static Object[][] provideMovieForEnrichmentSave() {

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

    @DataProvider(name = "provideMovieForEnrichmentSaveUniqueConstraint")
    static Object[][] provideMovieForEnrichmentSaveUniqueConstraint() {

        def movieExpected = new Movie(id: 100500, nameRussian: "Зеленая миля", nameNative: "The Green Mile", yearOfRelease: convertStringToDate("1999-01-01"),
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

    @DataProvider(name = "provideMovieForUpdateSaveUniqueConstraint")
    static Object[][] provideMovieForUpdateSaveUniqueConstraint() {

        def movieExpected = new Movie(id: 1, nameRussian: "Зеленая миля", nameNative: "The Green Mile", yearOfRelease: convertStringToDate("1999-01-01"),
                description: "Обвиненный в страшном преступлении, Джон Коффи оказывается в блоке смертников тюрьмы «Холодная гора». Вновь прибывший обладал поразительным ростом и был пугающе спокоен, что, впрочем, никак не влияло на отношение к нему начальника блока Пола Эджкомба, привыкшего исполнять приговор.",
                rating: 0, price: 134.67, picturePath: "green_mile173_173.jpg",
                genres: [new Genre(1), new Genre(1)])

        def array = new Object[1][]
        array[0] = [movieExpected]
        return array
    }
}
