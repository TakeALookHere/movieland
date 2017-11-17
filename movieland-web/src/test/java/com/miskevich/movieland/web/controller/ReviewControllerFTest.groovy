package com.miskevich.movieland.web.controller

import com.miskevich.movieland.service.IReviewService
import com.miskevich.movieland.service.IUserService
import com.miskevich.movieland.web.controller.provider.ControllerDataProvider
import com.miskevich.movieland.web.dto.ReviewDto
import com.miskevich.movieland.web.json.JsonConverter
import com.miskevich.movieland.service.security.UserPrincipal
import org.mockito.InjectMocks
import org.mockito.Mock
import org.mockito.MockitoAnnotations
import org.springframework.http.MediaType
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.setup.MockMvcBuilders
import org.springframework.web.util.NestedServletException
import org.testng.annotations.BeforeTest
import org.testng.annotations.Test

import static org.mockito.Mockito.*
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status

class ReviewControllerFTest {

    @Mock
    private IReviewService mockReviewService
    @Mock
    private IUserService mockUserService
    @InjectMocks
    private ReviewController reviewController
    private MockMvc mockMvc

    @BeforeTest
    private void setup() {
        MockitoAnnotations.initMocks(this)
        mockMvc = MockMvcBuilders.standaloneSetup(reviewController).build()
    }

    @Test(dataProvider = 'provideReviewAddInvalidRole', dataProviderClass = ControllerDataProvider.class,
            expectedExceptionsMessageRegExp = '.*Validation of user\'s role access type failed, required role: USER/ADMIN',
            expectedExceptions = NestedServletException.class)
    void testAddInvalidRole(ReviewDto reviewJson, String uuid, roleInvalid, UserPrincipal principal) {
        when(mockUserService.getRole(1)).thenReturn(roleInvalid)
        mockMvc.perform(post("/review")
                .header('uuid', uuid)
                .content(JsonConverter.toJson(reviewJson))
                .principal(principal)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))

        verify(mockUserService, times(1)).getRole(1)
        verifyNoMoreInteractions(mockUserService)
    }

    @Test(dataProvider = 'provideReviewAddSuccess', dataProviderClass = ControllerDataProvider.class)
    void testAddSuccess(roleValid, ReviewDto reviewJson,
                        String uuid, UserPrincipal principal) {
        when(mockUserService.getRole(1)).thenReturn(roleValid)
        mockMvc.perform(post("/review")
                .header('uuid', uuid)
                .content(JsonConverter.toJson(reviewJson))
                .principal(principal)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())

        verify(mockUserService, times(2)).getRole(1)
        verifyNoMoreInteractions(mockUserService)
    }
}
