package com.miskevich.movieland.dao.jdbc

import com.miskevich.movieland.dao.jdbc.provider.JdbcDataProvider
import com.miskevich.movieland.dao.jdbc.provider.SQLDataProvider
import com.miskevich.movieland.entity.Review
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.test.context.ContextConfiguration
import org.springframework.test.context.testng.AbstractTestNGSpringContextTests
import org.testng.annotations.Test

import static org.testng.Assert.assertNotNull

@ContextConfiguration(locations = "classpath:spring/jdbc-context.xml")
class JdbcReviewDaoITest extends AbstractTestNGSpringContextTests {

    @Autowired
    private JdbcReviewDao jdbcReviewDao

    @Test
    void testGetByMovieId() {
        def reviews = jdbcReviewDao.getByMovieId(24)
        for (Review review : reviews) {
            assertNotNull(review.getText())
            assertNotNull(review.getMovie())
            assertNotNull(review.getUser())
        }
    }

    @Test(dataProvider = "provideAddReview", dataProviderClass = JdbcDataProvider.class)
    void testAdd(Review reviewForAdd) {
        jdbcReviewDao.add(reviewForAdd)
    }

    @Test(dataProvider = 'provideMovieForEnrichmentSave', dataProviderClass = SQLDataProvider.class)
    void testSaveMovieGenresDuplicateKey(movie) {
        jdbcReviewDao.persist(movie)
    }

    @Test(dataProvider = 'provideMovieForEnrichmentUpdate', dataProviderClass = SQLDataProvider.class)
    void testUpdateMovieGenresDuplicateKey(movie) {
        jdbcReviewDao.update(movie)
    }
}
