package com.miskevich.movieland.dao.jdbc

import com.miskevich.movieland.entity.Country
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.test.context.ContextConfiguration
import org.springframework.test.context.testng.AbstractTestNGSpringContextTests
import org.testng.annotations.Test

import static org.testng.Assert.assertNotNull

@ContextConfiguration(locations = "classpath:spring/jdbc-context.xml")
class JdbcCountryDaoITest extends AbstractTestNGSpringContextTests {

    @Autowired
    private JdbcCountryDao jdbcCountryDao

    @Test
    void testGetByMovieId() {
        def countries = jdbcCountryDao.getByMovieId(6)
        for (Country country : countries) {
            assertNotNull(country.getName())
        }
    }
}
