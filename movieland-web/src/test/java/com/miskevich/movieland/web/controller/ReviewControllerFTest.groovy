package com.miskevich.movieland.web.controller

import com.miskevich.movieland.entity.Review
import com.miskevich.movieland.service.IReviewService
import com.miskevich.movieland.service.security.UserPrincipal
import com.miskevich.movieland.web.controller.provider.ControllerDataProvider
import com.miskevich.movieland.web.dto.ReviewDto
import com.miskevich.movieland.web.json.JsonConverter
import org.mockito.InjectMocks
import org.mockito.Mock
import org.mockito.MockitoAnnotations
import org.springframework.http.MediaType
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.setup.MockMvcBuilders
import org.testng.annotations.BeforeTest
import org.testng.annotations.Test

import static org.hamcrest.core.Is.is
import static org.mockito.Matchers.any
import static org.mockito.Mockito.when
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status

class ReviewControllerFTest {

    @Mock
    private IReviewService mockReviewService
    @InjectMocks
    private ReviewController reviewController
    private MockMvc mockMvc

    @BeforeTest
    private void setup() {
        MockitoAnnotations.initMocks(this)
        mockMvc = MockMvcBuilders.standaloneSetup(reviewController).build()
    }

    @Test(dataProvider = 'provideReviewAddSuccess', dataProviderClass = ControllerDataProvider.class)
    void testAddSuccess(ReviewDto reviewJson,
                        String uuid, UserPrincipal principal, Review expectedReview) {
        when(mockReviewService.add(any(Review.class))).thenReturn(expectedReview)
        mockMvc.perform(post("/review")
                .header('uuid', uuid)
                .content(JsonConverter.toJson(reviewJson))
                .principal(principal)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())

                .andExpect(jsonPath('$.id').exists())
                .andExpect(jsonPath('$.text', is(expectedReview.text)))
                .andExpect(jsonPath('$.movie.id', is(expectedReview.movie.id)))
                .andExpect(jsonPath('$.user.id', is(expectedReview.user.id)))
    }
}
