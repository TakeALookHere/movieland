package com.miskevich.movieland.web.controller

import com.miskevich.movieland.entity.Genre
import com.miskevich.movieland.service.IGenreService
import com.miskevich.movieland.web.controller.provider.DataProviderController
import org.mockito.InjectMocks
import org.mockito.Mock
import org.mockito.MockitoAnnotations
import org.springframework.http.MediaType
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.setup.MockMvcBuilders
import org.testng.annotations.BeforeTest
import org.testng.annotations.Test

import static org.hamcrest.Matchers.hasSize
import static org.hamcrest.core.Is.is
import static org.mockito.Mockito.*
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status

class GenreControllerTest {

    @Mock
    private IGenreService mockGenreService
    @InjectMocks
    private GenreController genreController
    private MockMvc mockMvc

    @BeforeTest
    private void setup() {
        MockitoAnnotations.initMocks(this)
        mockMvc = MockMvcBuilders.standaloneSetup(genreController).build()
    }

    @Test(dataProvider = "provideGenres", dataProviderClass = DataProviderController.class)
    void testGetAllGenres(List<Genre> expectedGenres) {
        when(mockGenreService.getAll()).thenReturn(expectedGenres)
        mockMvc.perform(get("/genre").accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath('$', hasSize(2)))

                .andExpect(jsonPath('$[0].id', is(1)))
                .andExpect(jsonPath('$[0].name', is('драма')))

                .andExpect(jsonPath('$[1].id', is(2)))
                .andExpect(jsonPath('$[1].name', is('криминал')))

        verify(mockGenreService, times(1)).getAll()
        verifyNoMoreInteractions(mockGenreService)
    }
}
