package com.miskevich.movieland.web.controller.provider

import com.miskevich.movieland.entity.Country
import com.miskevich.movieland.entity.Genre
import com.miskevich.movieland.entity.Movie
import org.testng.annotations.DataProvider

import java.time.LocalDate
import java.time.format.DateTimeFormatter

class DataProviderController {
    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd", Locale.ENGLISH)

    private static LocalDate convertStringToDate(String releasedDate) {
        return LocalDate.parse(releasedDate, DATE_FORMATTER)
    }

    @DataProvider(name = "provideMovies")
    static Object[][] provideMovies() {

        def expectedMovies = [
                new Movie(id: 1, nameRussian: "Побег из Шоушенка", nameNative: "The Shawshank Redemption", yearOfRelease: convertStringToDate("1994-01-01"),
                        description: "Успешный банкир Энди Дюфрейн обвинен в убийстве собственной жены и ее любовника. Оказавшись в тюрьме под названием Шоушенк, он сталкивается с жестокостью и беззаконием, царящими по обе стороны решетки. Каждый, кто попадает в эти стены, становится их рабом до конца жизни. Но Энди, вооруженный живым умом и доброй душой, отказывается мириться с приговором судьбы и начинает разрабатывать невероятно дерзкий план своего освобождения.",
                        rating: 9.1, price: 123.45, picturePath: "shawshank173_173.jpg"),

                new Movie(id: 2, nameRussian: "Зеленая миля", nameNative: "The Green Mile", yearOfRelease: convertStringToDate("1999-01-01"),
                        description: "Обвиненный в страшном преступлении, Джон Коффи оказывается в блоке смертников тюрьмы «Холодная гора». Вновь прибывший обладал поразительным ростом и был пугающе спокоен, что, впрочем, никак не влияло на отношение к нему начальника блока Пола Эджкомба, привыкшего исполнять приговор.",
                        rating: 8.9, price: 134.67, picturePath: "green_mile173_173.jpg")
        ]

        def array = new Object[1][]
        array[0] = [expectedMovies]
        return array
    }

    @DataProvider(name = "provideMoviesWithRequestParams")
    static Object[][] provideMoviesWithRequestParams() {

        def expectedMovies = [
                new Movie(id: 1, nameRussian: "Побег из Шоушенка", nameNative: "The Shawshank Redemption", yearOfRelease: convertStringToDate("1994-01-01"),
                        description: "Успешный банкир Энди Дюфрейн обвинен в убийстве собственной жены и ее любовника. Оказавшись в тюрьме под названием Шоушенк, он сталкивается с жестокостью и беззаконием, царящими по обе стороны решетки. Каждый, кто попадает в эти стены, становится их рабом до конца жизни. Но Энди, вооруженный живым умом и доброй душой, отказывается мириться с приговором судьбы и начинает разрабатывать невероятно дерзкий план своего освобождения.",
                        rating: 9.1, price: 123.45, picturePath: "shawshank173_173.jpg"),

                new Movie(id: 2, nameRussian: "Зеленая миля", nameNative: "The Green Mile", yearOfRelease: convertStringToDate("1999-01-01"),
                        description: "Обвиненный в страшном преступлении, Джон Коффи оказывается в блоке смертников тюрьмы «Холодная гора». Вновь прибывший обладал поразительным ростом и был пугающе спокоен, что, впрочем, никак не влияло на отношение к нему начальника блока Пола Эджкомба, привыкшего исполнять приговор.",
                        rating: 8.9, price: 134.67, picturePath: "green_mile173_173.jpg")
        ]

        def params = [
                price       : 'asc',
                name_russian: 'DESC',
                RATING      : 'desc'
        ]

        def array = new Object[1][]
        array[0] = [expectedMovies, params]
        return array
    }

    @DataProvider(name = "provideMoviesWithGenreAndCountry")
    static Object[][] provideMoviesWithGenreAndCountry() {

        def expectedMovies = [
                new Movie(id: 1, nameRussian: "Побег из Шоушенка", nameNative: "The Shawshank Redemption", yearOfRelease: convertStringToDate("1994-01-01"),
                        description: "Успешный банкир Энди Дюфрейн обвинен в убийстве собственной жены и ее любовника. Оказавшись в тюрьме под названием Шоушенк, он сталкивается с жестокостью и беззаконием, царящими по обе стороны решетки. Каждый, кто попадает в эти стены, становится их рабом до конца жизни. Но Энди, вооруженный живым умом и доброй душой, отказывается мириться с приговором судьбы и начинает разрабатывать невероятно дерзкий план своего освобождения.",
                        rating: 9.1, price: 123.45, picturePath: "shawshank173_173.jpg",
                        genres: [new Genre(1, 'драма'), new Genre(2, 'криминал')],
                        countries: [new Country(id: 1, name: 'США'), new Country(id: 2, name: 'Франция')]),

                new Movie(id: 2, nameRussian: "Зеленая миля", nameNative: "The Green Mile", yearOfRelease: convertStringToDate("1999-01-01"),
                        description: "Обвиненный в страшном преступлении, Джон Коффи оказывается в блоке смертников тюрьмы «Холодная гора». Вновь прибывший обладал поразительным ростом и был пугающе спокоен, что, впрочем, никак не влияло на отношение к нему начальника блока Пола Эджкомба, привыкшего исполнять приговор.",
                        rating: 8.9, price: 134.67, picturePath: "green_mile173_173.jpg",
                        genres: [new Genre(5, 'мелодрама')],
                        countries: [new Country(id: 3, name: 'Великобритания')])
        ]

        def array = new Object[1][]
        array[0] = [expectedMovies]
        return array
    }

    @DataProvider(name = "provideGenres")
    static Object[][] provideGenres() {

        def expectedGenres = [
                new Genre(1, "драма"),
                new Genre(2, "криминал"),
        ]

        def array = new Object[1][]
        array[0] = [expectedGenres]
        return array
    }
}
