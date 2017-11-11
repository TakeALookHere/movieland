package com.miskevich.movieland.web.controller

import com.miskevich.movieland.entity.User
import com.miskevich.movieland.service.IReviewService
import com.miskevich.movieland.service.IUserService
import com.miskevich.movieland.web.controller.provider.ControllerDataProvider
import org.mockito.InjectMocks
import org.mockito.Mock
import org.mockito.MockitoAnnotations
import org.springframework.http.MediaType
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.result.MockMvcResultMatchers
import org.springframework.test.web.servlet.setup.MockMvcBuilders
import org.springframework.web.util.NestedServletException
import org.testng.annotations.BeforeTest
import org.testng.annotations.Test

import static org.mockito.Mockito.*
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status

class ReviewControllerTest {

    @Mock
    private IReviewService mockReviewService
    @Mock
    private IUserService mockUserService
    @Mock
    private UserController mockUserController
    @InjectMocks
    private ReviewController reviewController
    private MockMvc mockMvc

    @BeforeTest
    private void setup() {
        MockitoAnnotations.initMocks(this)
        mockMvc = MockMvcBuilders.standaloneSetup(reviewController).build()
    }

    @Test(dataProvider = 'provideReviewAdd', dataProviderClass = ControllerDataProvider.class,
            expectedExceptionsMessageRegExp = '.*User has not applicable role: manager!',
            expectedExceptions = NestedServletException.class)
    void testAddIncorrectRole(Map<UUID, User> uuidCache, String roleValid, String reviewJson, UUID uuid, String roleInvalid, String roleIncorrect) {
        when(mockUserController.getUuidUserCache()).thenReturn(uuidCache)
        when(mockUserService.getRole(1)).thenReturn(roleIncorrect)
        mockMvc.perform(post("/review")
                .header('uuid', uuid)
                .content(reviewJson)
                .accept(MediaType.APPLICATION_JSON))

        verify(mockUserController, times(1)).getUuidUserCache()
        verifyNoMoreInteractions(mockUserController)

        verify(mockUserService, times(1)).getRole(1)
        verifyNoMoreInteractions(mockUserService)
    }

    @Test(dataProvider = 'provideReviewAdd', dataProviderClass = ControllerDataProvider.class,
            expectedExceptionsMessageRegExp = '.*WARN: validation of user\'s role access type failed, required role: USER',
            expectedExceptions = NestedServletException.class)
    void testAddInvalidRole(Map<UUID, User> uuidCache, String roleValid, String reviewJson, UUID uuid, String roleInvalid, String roleIncorrect) {
        when(mockUserController.getUuidUserCache()).thenReturn(uuidCache)
        when(mockUserService.getRole(1)).thenReturn(roleInvalid)
        mockMvc.perform(post("/review")
                .header('uuid', uuid)
                .content(reviewJson)
                .accept(MediaType.APPLICATION_JSON))

        verify(mockUserController, times(2)).getUuidUserCache()
        verifyNoMoreInteractions(mockUserController)

        verify(mockUserService, times(2)).getRole(1)
        verifyNoMoreInteractions(mockUserService)
    }

    @Test(dataProvider = 'provideReviewAdd', dataProviderClass = ControllerDataProvider.class)
    void testAddSuccess(Map<UUID, User> uuidCache, String roleValid, String reviewJson, UUID uuid, String roleInvalid, String roleIncorrect) {
        when(mockUserController.getUuidUserCache()).thenReturn(uuidCache)
        when(mockUserService.getRole(1)).thenReturn(roleValid)
        mockMvc.perform(post("/review")
                .header('uuid', uuid)
                .content(reviewJson)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())

        verify(mockUserController, times(3)).getUuidUserCache()
        verifyNoMoreInteractions(mockUserController)

        verify(mockUserService, times(3)).getRole(1)
        verifyNoMoreInteractions(mockUserService)
    }

    @Test(dataProvider = 'provideReviewAdd', dataProviderClass = ControllerDataProvider.class)
    void testAddUuidExpired(Map<UUID, User> uuidCache, String roleValid, String reviewJson, UUID uuid, String roleInvalid, String roleIncorrect) {
        when(mockUserController.getUuidUserCache()).thenReturn(uuidCache)
        mockMvc.perform(post("/review")
                .header('uuid', '8495da92-093c-4e5d-b178-82da08b66d7b')
                .content(reviewJson)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.redirectedUrl('/v1/login'))

        verify(mockUserController, times(4)).getUuidUserCache()
        verifyNoMoreInteractions(mockUserController)
    }
}
