package com.miskevich.movieland.web.controller

import com.miskevich.movieland.entity.User
import com.miskevich.movieland.service.IUserService
import com.miskevich.movieland.web.controller.provider.DataProviderController
import org.mockito.InjectMocks
import org.mockito.Mock
import org.mockito.MockitoAnnotations
import org.springframework.dao.EmptyResultDataAccessException
import org.springframework.http.MediaType
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.setup.MockMvcBuilders
import org.testng.annotations.BeforeTest
import org.testng.annotations.Test

import static org.hamcrest.core.Is.is
import static org.mockito.Mockito.*
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status

class UserControllerTest {

    @Mock
    private IUserService mockUserService
    @InjectMocks
    private UserController userController
    private MockMvc mockMvc

    @BeforeTest
    private void setup() {
        MockitoAnnotations.initMocks(this)
        mockMvc = MockMvcBuilders.standaloneSetup(userController).build()
    }

    @Test(dataProvider = 'provideUser', dataProviderClass = DataProviderController.class)
    void testLogin(User expectedUser) {
        def email = 'ronald.reynolds66@example.com'
        def password = 'paco'

        when(mockUserService.getByEmailAndPassword(email, password)).thenReturn(expectedUser)
        mockMvc.perform(post("/login")
                .param('email', email)
                .param('password', password)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())

                .andExpect(jsonPath('$.nickname', is(expectedUser.nickname)))
                .andExpect(jsonPath('$.password').doesNotExist())
                .andExpect(jsonPath('$.id').doesNotExist())
                .andExpect(jsonPath('$.uuid').exists())

        verify(mockUserService, times(1)).getByEmailAndPassword(email, password)
        verifyNoMoreInteractions(mockUserService)
    }

    @Test
    void testLoginBadRequest() {
        def email = 'ronald.reynolds66@example.com'
        def password = 'paco'

        when(mockUserService.getByEmailAndPassword(email, password)).thenThrow(EmptyResultDataAccessException)
        mockMvc.perform(post("/login")
                .param('email', email)
                .param('password', password)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())

                .andExpect(jsonPath('$.nickname').doesNotExist())
                .andExpect(jsonPath('$.password').doesNotExist())
                .andExpect(jsonPath('$.id').doesNotExist())
                .andExpect(jsonPath('$.uuid').doesNotExist())

        verify(mockUserService, times(2)).getByEmailAndPassword(email, password)
        verifyNoMoreInteractions(mockUserService)
    }

    @Test
    void testLogout() {
        mockMvc.perform(delete("/logout")
                .header('X-Request-ID', '8495da92-093c-4e5d-b178-82da08b66d7b')
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
    }
}
