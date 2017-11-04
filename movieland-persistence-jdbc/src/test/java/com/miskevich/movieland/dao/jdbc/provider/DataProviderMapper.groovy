package com.miskevich.movieland.dao.jdbc.provider

import com.miskevich.movieland.entity.Country
import com.miskevich.movieland.entity.Genre
import com.miskevich.movieland.entity.Movie
import org.testng.annotations.DataProvider

import java.time.LocalDate
import java.time.format.DateTimeFormatter

class DataProviderMapper {

    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd", Locale.ENGLISH)

    private static LocalDate convertStringToDate(String dateOfBirth) {
        return LocalDate.parse(dateOfBirth, DATE_FORMATTER)
    }

    @DataProvider(name = "provideMovie")
    static Object[][] provideMovie() {

        def expectedMovie = new Movie(id: 1, nameRussian: "Побег из Шоушенка", nameNative: "The Shawshank Redemption",
                yearOfRelease: convertStringToDate("2016-01-15"), description: "Успешный банкир Энди Дюфрейн обвинен в убийстве собственной жены и ее любовника. Оказавшись в тюрьме под названием Шоушенк, он сталкивается с жестокостью и беззаконием, царящими по обе стороны решетки. Каждый, кто попадает в эти стены, становится их рабом до конца жизни. Но Энди, вооруженный живым умом и доброй душой, отказывается мириться с приговором судьбы и начинает разрабатывать невероятно дерзкий план своего освобождения.",
                rating: 8.9, price: 123.45, picturePath: "shawshank173_173.jpg")

        def array = new Object[1][]
        array[0] = expectedMovie
        return array
    }

    @DataProvider(name = "provideCountry")
    static Object[][] provideCountry() {

        def expectedCountry = new Country(id: 1, name: 'США')

        def array = new Object[1][]
        array[0] = expectedCountry
        return array
    }

    @DataProvider(name = "provideGenre")
    static Object[][] provideGenre() {

        def expectedGenre = new Genre(1, 'драма')

        def array = new Object[1][]
        array[0] = expectedGenre
        return array
    }
}
