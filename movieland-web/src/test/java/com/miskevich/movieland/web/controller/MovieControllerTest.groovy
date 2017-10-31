package com.miskevich.movieland.web.controller

import com.miskevich.movieland.entity.Movie
import com.miskevich.movieland.service.IMovieService
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

class MovieControllerTest {

    @Mock
    private IMovieService mockMovieService
    @InjectMocks
    private MovieController movieController
    private MockMvc mockMvc

    @BeforeTest
    private void setup() {
        MockitoAnnotations.initMocks(this)
        mockMvc = MockMvcBuilders.standaloneSetup(movieController).build()
    }

    @Test(dataProvider = "provideMovies", dataProviderClass = DataProviderController.class)
    void testGetAllMovies(List<Movie> expectedMovies) {

        when(mockMovieService.getAll()).thenReturn(expectedMovies)
        mockMvc.perform(get("/v1/movie").accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath('$', hasSize(2)))

                .andExpect(jsonPath('$[0].id', is(1)))
                .andExpect(jsonPath('$[0].nameRussian', is('Побег из Шоушенка')))
                .andExpect(jsonPath('$[0].nameNative', is('The Shawshank Redemption')))
                .andExpect(jsonPath('$[0].releasedDate', is('1994')))
                .andExpect(jsonPath('$[0].plot', is('Успешный банкир Энди Дюфрейн обвинен в убийстве собственной жены и ее любовника. Оказавшись в тюрьме под названием Шоушенк, он сталкивается с жестокостью и беззаконием, царящими по обе стороны решетки. Каждый, кто попадает в эти стены, становится их рабом до конца жизни. Но Энди, вооруженный живым умом и доброй душой, отказывается мириться с приговором судьбы и начинает разрабатывать невероятно дерзкий план своего освобождения.')))
                .andExpect(jsonPath('$[0].rating', is(8.9d)))
                .andExpect(jsonPath('$[0].price', is(123.45d)))
                .andExpect(jsonPath('$[0].picturePath', is('shawshank173_173.jpg')))

                .andExpect(jsonPath('$[1].id', is(2)))
                .andExpect(jsonPath('$[1].nameRussian', is('Зеленая миля')))
                .andExpect(jsonPath('$[1].nameNative', is('The Green Mile')))
                .andExpect(jsonPath('$[1].releasedDate', is('1999')))
                .andExpect(jsonPath('$[1].plot', is('Обвиненный в страшном преступлении, Джон Коффи оказывается в блоке смертников тюрьмы «Холодная гора». Вновь прибывший обладал поразительным ростом и был пугающе спокоен, что, впрочем, никак не влияло на отношение к нему начальника блока Пола Эджкомба, привыкшего исполнять приговор.')))
                .andExpect(jsonPath('$[1].rating', is(8.9d)))
                .andExpect(jsonPath('$[1].price', is(134.67d)))
                .andExpect(jsonPath('$[1].picturePath', is('green_mile173_173.jpg')))

        verify(mockMovieService, times(1)).getAll()
        verifyNoMoreInteractions(mockMovieService)
    }

    @Test(dataProvider = "provideMovies", dataProviderClass = DataProviderController.class)
    void testGetThreeRandomMovies(List<Movie> expectedMovies) {

        when(mockMovieService.getThreeRandomMovies()).thenReturn(expectedMovies)
        mockMvc.perform(get("/v1/movie/random").accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath('$', hasSize(2)))

                .andExpect(jsonPath('$[0].id', is(1)))
                .andExpect(jsonPath('$[0].nameRussian', is('Побег из Шоушенка')))
                .andExpect(jsonPath('$[0].nameNative', is('The Shawshank Redemption')))
                .andExpect(jsonPath('$[0].releasedDate', is('1994')))
                .andExpect(jsonPath('$[0].plot', is('Успешный банкир Энди Дюфрейн обвинен в убийстве собственной жены и ее любовника. Оказавшись в тюрьме под названием Шоушенк, он сталкивается с жестокостью и беззаконием, царящими по обе стороны решетки. Каждый, кто попадает в эти стены, становится их рабом до конца жизни. Но Энди, вооруженный живым умом и доброй душой, отказывается мириться с приговором судьбы и начинает разрабатывать невероятно дерзкий план своего освобождения.')))
                .andExpect(jsonPath('$[0].rating', is(8.9d)))
                .andExpect(jsonPath('$[0].price', is(123.45d)))
                .andExpect(jsonPath('$[0].picturePath', is('shawshank173_173.jpg')))

                .andExpect(jsonPath('$[1].id', is(2)))
                .andExpect(jsonPath('$[1].nameRussian', is('Зеленая миля')))
                .andExpect(jsonPath('$[1].nameNative', is('The Green Mile')))
                .andExpect(jsonPath('$[1].releasedDate', is('1999')))
                .andExpect(jsonPath('$[1].plot', is('Обвиненный в страшном преступлении, Джон Коффи оказывается в блоке смертников тюрьмы «Холодная гора». Вновь прибывший обладал поразительным ростом и был пугающе спокоен, что, впрочем, никак не влияло на отношение к нему начальника блока Пола Эджкомба, привыкшего исполнять приговор.')))
                .andExpect(jsonPath('$[1].rating', is(8.9d)))
                .andExpect(jsonPath('$[1].price', is(134.67d)))
                .andExpect(jsonPath('$[1].picturePath', is('green_mile173_173.jpg')))

        verify(mockMovieService, times(1)).getThreeRandomMovies()
        verifyNoMoreInteractions(mockMovieService)
    }
}
