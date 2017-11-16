package com.miskevich.movieland.service.provider

import com.miskevich.movieland.entity.User
import org.testng.annotations.DataProvider

class ServiceDataProvider {

    @DataProvider(name = "putUserIntoCache")
    static Object[][] putUserIntoCache() {

        def user = new User(1, 'Super User', 'ronald.reynolds66@example.com')

        def array = new Object[1][]
        array[0] = [user]
        return array
    }
}
