package com.miskevich.movieland.dao.jdbc.provider

import com.miskevich.movieland.entity.Movie
import com.miskevich.movieland.entity.Review
import com.miskevich.movieland.entity.User
import org.testng.annotations.DataProvider

class JdbcDataProvider {

    @DataProvider(name = "provideAddReview")
    static Object[][] provideAddReview(){
        def reviewForAdd = new Review(
                text: "Тестовый дескрипшен для ревью",
                movie: new Movie(id: 1),
                user: new User(id: 3))

        def array = new Object[1][]
        array[0] = reviewForAdd
        return array
    }
}
