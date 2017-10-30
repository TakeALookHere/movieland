package com.miskevich.movieland.web.controller.provider

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
                new Movie(id: 1, nameRussian: "Побег из Шоушенка", nameNative: "The Shawshank Redemption", releasedDate: convertStringToDate("1994-01-01"),
                        plot: "Успешный банкир Энди Дюфрейн обвинен в убийстве собственной жены и ее любовника. Оказавшись в тюрьме под названием Шоушенк, он сталкивается с жестокостью и беззаконием, царящими по обе стороны решетки. Каждый, кто попадает в эти стены, становится их рабом до конца жизни. Но Энди, вооруженный живым умом и доброй душой, отказывается мириться с приговором судьбы и начинает разрабатывать невероятно дерзкий план своего освобождения.",
                        rating: 8.9, price: 123.45, picturePath: "shawshank173_173.jpg"),

                new Movie(id: 2, nameRussian: "Зеленая миля", nameNative: "The Green Mile", releasedDate: convertStringToDate("1999-01-01"),
                        plot: "Обвиненный в страшном преступлении, Джон Коффи оказывается в блоке смертников тюрьмы «Холодная гора». Вновь прибывший обладал поразительным ростом и был пугающе спокоен, что, впрочем, никак не влияло на отношение к нему начальника блока Пола Эджкомба, привыкшего исполнять приговор.",
                        rating: 8.9, price: 134.67, picturePath: "green_mile173_173.jpg"),
        ]

        def array = new Object[1][]
        array[0] = [expectedMovies]
        return array
    }

    @DataProvider(name = "provideGenres")
    static Object[][] provideGenres() {

        def expectedGenres = [
                new Genre(id: 1, name: "драма"),
                new Genre(id: 2, name: "криминал"),
        ]

        def array = new Object[1][]
        array[0] = [expectedGenres]
        return array
    }
}
