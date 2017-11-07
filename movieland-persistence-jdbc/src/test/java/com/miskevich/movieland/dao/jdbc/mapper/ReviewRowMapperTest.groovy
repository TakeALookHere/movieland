package com.miskevich.movieland.dao.jdbc.mapper

import com.miskevich.movieland.dao.jdbc.provider.MapperDataProvider
import com.miskevich.movieland.entity.Review
import org.testng.annotations.Test

import java.sql.ResultSet

import static org.mockito.Mockito.mock
import static org.mockito.Mockito.when
import static org.testng.Assert.assertEquals

class ReviewRowMapperTest {

    private ReviewRowMapper reviewRowMapper = new ReviewRowMapper()

    @Test(dataProvider = 'provideReview', dataProviderClass = MapperDataProvider.class)
    void testMapRow(Review expectedReview) {
        ResultSet resultSet = mock(ResultSet.class)
        when(resultSet.next()).thenReturn(true).thenReturn(false)
        when(resultSet.getLong("id")).thenReturn(1l)
        when(resultSet.getString("description")).thenReturn("Гениальное кино! Смотришь и думаешь «Так не бывает!», но позже понимаешь, что только так и должно быть. Начинаешь заново осмысливать значение фразы, которую постоянно используешь в своей жизни, «Надежда умирает последней». Ведь если ты не надеешься, то все в твоей жизни гаснет, не остается смысла. Фильм наполнен бесконечным числом правильных афоризмов. Я уверена, что буду пересматривать его сотни раз.")
        when(resultSet.getInt("movie_id")).thenReturn(1)
        when(resultSet.getInt("user_id")).thenReturn(3)

        def actualReview = reviewRowMapper.mapRow(resultSet, 0)
        assertEquals(actualReview.getId(), expectedReview.getId())
        assertEquals(actualReview.getText(), expectedReview.getText())
        assertEquals(actualReview.getMovie().getId(), expectedReview.getMovie().getId())
        assertEquals(actualReview.getUser().getId(), expectedReview.getUser().getId())
    }
}
