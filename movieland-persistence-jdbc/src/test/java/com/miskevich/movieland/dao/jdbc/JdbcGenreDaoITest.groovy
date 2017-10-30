package com.miskevich.movieland.dao.jdbc

import com.miskevich.movieland.entity.Genre
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.test.context.ContextConfiguration
import org.springframework.test.context.testng.AbstractTestNGSpringContextTests
import org.testng.annotations.Test

import static org.testng.Assert.assertNotNull

@ContextConfiguration(locations = "classpath:spring/jdbc-context.xml")
class JdbcGenreDaoITest extends AbstractTestNGSpringContextTests{

    @Autowired
    private JdbcGenreDao jdbcGenreDao

    @Test
    void testGetAll() {
        def genres = jdbcGenreDao.getAll()
        for (Genre genre : genres){
            assertNotNull(genre.getId())
            assertNotNull(genre.getName())
        }
    }
}
