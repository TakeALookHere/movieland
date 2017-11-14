package com.miskevich.movieland.web.controller

import com.miskevich.movieland.entity.User
import com.miskevich.movieland.service.IUserService
import com.miskevich.movieland.service.impl.UserSecurityService
import com.miskevich.movieland.web.controller.provider.ControllerDataProvider
import com.miskevich.movieland.web.exception.InvalidUserException
import com.miskevich.movieland.web.helper.FunctionalTestUtil
import org.mockito.InjectMocks
import org.mockito.Mock
import org.mockito.MockitoAnnotations
import org.springframework.dao.EmptyResultDataAccessException
import org.springframework.http.MediaType
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.setup.MockMvcBuilders
import org.springframework.web.util.NestedServletException
import org.testng.annotations.BeforeTest
import org.testng.annotations.Test

import static org.testng.Assert.assertEquals
import static org.hamcrest.core.Is.is
import static org.mockito.Mockito.*
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status

class UserControllerTest {

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

    @Test(dataProvider = 'provideUserJson', dataProviderClass = ControllerDataProvider.class)
    void testLogin(String email, String password, User userJson, User expectedUser, UUID uuid) {

        when(mockUserService.getByEmailAndPassword(email, password)).thenReturn(expectedUser)
        when(mockUserSecurityService.putUserIntoCache(expectedUser)).thenReturn(uuid)
        mockMvc.perform(post("/login")
                .content(FunctionalTestUtil.convertObjectToJsonBytes(userJson))
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())

                .andExpect(jsonPath('$.nickname', is(expectedUser.nickname)))
                .andExpect(jsonPath('$.password').doesNotExist())
                .andExpect(jsonPath('$.id').doesNotExist())
                .andExpect(jsonPath('$.uuid', is(uuid.toString())))

        verify(mockUserService, times(1)).getByEmailAndPassword(email, password)
        verifyNoMoreInteractions(mockUserService)
    }

    @Test(dataProvider = 'provideUserJson', dataProviderClass = ControllerDataProvider.class,
            expectedExceptionsMessageRegExp = ".*No user in DB with such pair of email and password was found: ronald.reynolds66@example.com and paco",
            expectedExceptions = NestedServletException.class)
    void testLoginBadRequest(String email, String password, User userJson, User expectedUser, UUID uuid) {
        when(mockUserService.getByEmailAndPassword(email, password)).thenThrow(EmptyResultDataAccessException)
        mockMvc.perform(post("/login")
                .content(FunctionalTestUtil.convertObjectToJsonBytes(userJson))
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())

        verify(mockUserService, times(2)).getByEmailAndPassword(email, password)
        verifyNoMoreInteractions(mockUserService)
    }

    @Test
    void testLogout() {
        mockMvc.perform(delete("/logout")
                .header('uuid', '8495da92-093c-4e5d-b178-82da08b66d7b')
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
    }


}
