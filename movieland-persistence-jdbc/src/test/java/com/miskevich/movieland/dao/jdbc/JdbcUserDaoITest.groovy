package com.miskevich.movieland.dao.jdbc

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.test.context.ContextConfiguration
import org.springframework.test.context.testng.AbstractTestNGSpringContextTests
import org.testng.annotations.Test

import static org.testng.Assert.assertNotNull

@ContextConfiguration(locations = "classpath:spring/jdbc-context.xml")
class JdbcUserDaoITest extends AbstractTestNGSpringContextTests {

    @Autowired
    private JdbcUserDao jdbcUserDao

    @Test
    void testGetByEmailAndPassword() {
        def user = jdbcUserDao.getByEmailAndPassword('ronald.reynolds66@example.com', 'paco')
        assertNotNull(user.getNickname())
        assertNotNull(user.getEmail())
        assertNotNull(user.getPassword())
    }
}
