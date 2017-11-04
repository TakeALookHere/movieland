package com.miskevich.movieland.dao.jdbc

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
            assertNotNull(review.getId())
            assertNotNull(review.getDescription())
            assertNotNull(review.getMovie())
            assertNotNull(review.getUser())
        }
    }
}
