package com.miskevich.movieland.web.controller

import com.miskevich.movieland.entity.User
import com.miskevich.movieland.service.IUserService
import com.miskevich.movieland.service.impl.UserSecurityService
import com.miskevich.movieland.web.controller.provider.ControllerDataProvider
import com.miskevich.movieland.web.exception.InvalidUserException
import org.mockito.InjectMocks
import org.mockito.Mock
import org.mockito.MockitoAnnotations
import org.springframework.dao.EmptyResultDataAccessException
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.setup.MockMvcBuilders
import org.testng.annotations.BeforeTest
import org.testng.annotations.Test

import static org.mockito.Matchers.anyString
import static org.mockito.Mockito.*
import static org.testng.Assert.assertEquals

class UserControllerUTest {

    @Mock
    private IUserService mockUserService
    @Mock
    private UserSecurityService mockUserSecurityService
    @InjectMocks
    private UserController userController
    private MockMvc mockMvc

    @BeforeTest
    private void setup() {
        MockitoAnnotations.initMocks(this)
        mockMvc = MockMvcBuilders.standaloneSetup(userController).build()
    }

    @Test(dataProvider = 'userCredentials', dataProviderClass = ControllerDataProvider.class)
    void testAuthAndEnrich(Map<String, String> userCredentialsMap, User expectedUser) {
        when(mockUserService.getByEmailAndPassword(anyString(), anyString())).thenReturn(expectedUser)
        def actualUser = userController.authAndEnrich(userCredentialsMap)
        assertEquals(actualUser.getId(), expectedUser.getId())
        assertEquals(actualUser.getEmail(), expectedUser.getEmail())
        assertEquals(actualUser.getNickname(), expectedUser.getNickname())

        verify(mockUserService, times(1)).getByEmailAndPassword(anyString(), anyString())
        verifyNoMoreInteractions(mockUserService)
    }

    @Test(dataProvider = 'userCredentials', dataProviderClass = ControllerDataProvider.class,
            expectedExceptions = InvalidUserException.class,
            expectedExceptionsMessageRegExp = 'No user in DB with such pair of email and password was found: testEmail and testPassword')
    void testAuthAndEnrichNoUserInDB(Map<String, String> userCredentialsMap, User expectedUser) {
        when(mockUserService.getByEmailAndPassword(anyString(), anyString())).thenThrow(EmptyResultDataAccessException)
        userController.authAndEnrich(userCredentialsMap)
    }
}
