package com.miskevich.movieland.service.impl

import com.miskevich.movieland.entity.User
import com.miskevich.movieland.service.exception.AuthRequiredException
import com.miskevich.movieland.service.provider.ServiceDataProvider
import org.mockito.InjectMocks
import org.mockito.Mock
import org.mockito.MockitoAnnotations
import org.springframework.util.IdGenerator
import org.testng.annotations.BeforeTest
import org.testng.annotations.Test

import static org.mockito.Mockito.when
import static org.testng.Assert.assertEquals

class UserSecurityServiceFTest {

    @Mock
    private IdGenerator mockIdGenerator
    @InjectMocks
    private UserSecurityService userSecurityService

    @BeforeTest
    private void setup() {
        MockitoAnnotations.initMocks(this)
    }

    @Test(dataProvider = 'putUserIntoCache', dataProviderClass = ServiceDataProvider.class)
    void testPutUserIntoCache(User user) {
        when(mockIdGenerator.generateId()).thenReturn(UUID.fromString('8495da92-093c-4e5d-b178-82da08b66d7b'))
        userSecurityService.putUserIntoCache(user)
        User userFromCache = userSecurityService.getFromCache('8495da92-093c-4e5d-b178-82da08b66d7b').get()
        assertEquals(userFromCache.id, user.id)
        assertEquals(userFromCache.nickname, user.nickname)
        assertEquals(userFromCache.email, user.email)
    }

    @Test(expectedExceptionsMessageRegExp = '.*User with UUID 8495da92-093c-4e5d-b178-82da08b66d7b is not authorized, please login',
            expectedExceptions = AuthRequiredException.class)
    void testGetFromCacheAuthRequired() {
        userSecurityService.getFromCache('8495da92-093c-4e5d-b178-82da08b66d7b')
    }
}
