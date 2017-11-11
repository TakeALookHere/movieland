package com.miskevich.movieland.web.json

import com.miskevich.movieland.entity.User
import com.miskevich.movieland.web.controller.provider.JsonDataProvider
import org.testng.annotations.Test

import static org.testng.Assert.assertEquals

class JsonConverterTest {

    @Test(dataProvider = "provideObjectList", dataProviderClass = JsonDataProvider.class)
    void testToJsonListObject(movies, expectedJson) {
        def actualJson = JsonConverter.toJson(movies)
        assertEquals(actualJson, expectedJson)
    }

    @Test(dataProvider = "provideObject", dataProviderClass = JsonDataProvider.class)
    void testToJsonObject(movie, expectedJson) {
        def actualJson = JsonConverter.toJson(movie)
        assertEquals(actualJson, expectedJson)
    }

    @Test(dataProvider = "provideJson", dataProviderClass = JsonDataProvider.class)
    void testFromJson(String userJson, User expectedUser) {
        InputStream inputStream = new ByteArrayInputStream(userJson.getBytes())
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream))
        def actualUser = JsonConverter.fromJson(bufferedReader, User.class)
        assertEquals(actualUser.getId(), expectedUser.getId())
        assertEquals(actualUser.getEmail(), expectedUser.getEmail())
        assertEquals(actualUser.getPassword(), expectedUser.getPassword())
        assertEquals(actualUser.getNickname(), expectedUser.getNickname())
    }
}
