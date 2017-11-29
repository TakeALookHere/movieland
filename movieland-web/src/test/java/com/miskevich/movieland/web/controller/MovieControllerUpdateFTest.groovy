package com.miskevich.movieland.web.controller

import com.miskevich.movieland.entity.Movie
import com.miskevich.movieland.service.IMovieService
import com.miskevich.movieland.service.IUserService
import com.miskevich.movieland.service.security.UserPrincipal
import com.miskevich.movieland.web.controller.provider.ControllerDataProvider
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
import static org.mockito.Matchers.anyInt
import static org.mockito.Mockito.*
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status

class MovieControllerUpdateFTest {

    @Mock
    private IMovieService mockMovieService
    @Mock
    private IUserService mockUserService
    @InjectMocks
    private MovieController movieController
    private MockMvc mockMvc

    @BeforeTest
    private void setup() {
        MockitoAnnotations.initMocks(this)
        mockMvc = MockMvcBuilders.standaloneSetup(movieController).build()
    }

    @Test(dataProvider = "provideMovieUpdateSuccess", dataProviderClass = ControllerDataProvider.class)
    void testUpdateSuccess(roleValid, Movie movieExpected, movieJson, String uuid, UserPrincipal principal) {

        when(mockUserService.getRole(anyInt())).thenReturn(roleValid)
        when(mockMovieService.update(any(Movie.class))).thenReturn(movieExpected)
        mockMvc.perform(put("/movie/{movieId}", movieExpected.id)
                .header('uuid', uuid)
                .content(JsonConverter.toJson(movieJson))
                .principal(principal)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())

                .andExpect(jsonPath('$.id', is(movieExpected.id)))
                .andExpect(jsonPath('$.nameRussian', is(movieExpected.nameRussian)))
                .andExpect(jsonPath('$.nameNative', is(movieExpected.nameNative)))
                .andExpect(jsonPath('$.yearOfRelease', is('1999')))
                .andExpect(jsonPath('$.description', is(movieExpected.description)))
                .andExpect(jsonPath('$.rating', is(movieExpected.rating)))
                .andExpect(jsonPath('$.price', is(movieExpected.price)))
                .andExpect(jsonPath('$.picturePath', is(movieExpected.picturePath)))
                .andExpect(jsonPath('$.genres[0].id', is(movieExpected.genres.get(0).id)))
                .andExpect(jsonPath('$.genres[0].name', is(movieExpected.genres.get(0).name)))
                .andExpect(jsonPath('$.genres[1].id', is(movieExpected.genres.get(1).id)))
                .andExpect(jsonPath('$.genres[1].name', is(movieExpected.genres.get(1).name)))
                .andExpect(jsonPath('$.countries[0].id', is(movieExpected.countries.get(0).id)))
                .andExpect(jsonPath('$.countries[0].name', is(movieExpected.countries.get(0).name)))
                .andExpect(jsonPath('$.countries[1].id', is(movieExpected.countries.get(1).id)))
                .andExpect(jsonPath('$.countries[1].name', is(movieExpected.countries.get(1).name)))
                .andExpect(jsonPath('$.reviews[0].id', is(movieExpected.reviews.get(0).id.intValue())))
                .andExpect(jsonPath('$.reviews[0].text', is(movieExpected.reviews.get(0).text)))
                .andExpect(jsonPath('$.reviews[0].movie.id', is(movieExpected.reviews.get(0).movie.id)))
                .andExpect(jsonPath('$.reviews[0].user.id', is(movieExpected.reviews.get(0).user.id)))
                .andExpect(jsonPath('$.reviews[1].id', is(movieExpected.reviews.get(1).id.intValue())))
                .andExpect(jsonPath('$.reviews[1].text', is(movieExpected.reviews.get(1).text)))
                .andExpect(jsonPath('$.reviews[1].movie.id', is(movieExpected.reviews.get(1).movie.id)))
                .andExpect(jsonPath('$.reviews[1].user.id', is(movieExpected.reviews.get(1).user.id)))

        verify(mockUserService, times(1)).getRole(anyInt())
        verifyNoMoreInteractions(mockUserService)
        verify(mockMovieService, times(1)).update(any(Movie.class))
        verifyNoMoreInteractions(mockMovieService)
    }
}
