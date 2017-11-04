package com.miskevich.movieland.web.controller

import com.miskevich.movieland.entity.Movie
import com.miskevich.movieland.service.IMovieService
import com.miskevich.movieland.web.controller.provider.DataProviderController
import com.miskevich.movieland.web.dto.RateDto
import com.miskevich.movieland.web.util.RateReader
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
    @Mock
    private RateReader rateReader

    @BeforeTest
    private void setup() {
        MockitoAnnotations.initMocks(this)
        mockMvc = MockMvcBuilders.standaloneSetup(movieController).build()
    }

    @Test(dataProvider = "provideMovies", dataProviderClass = DataProviderController.class)
    void testGetAllMovies(List<Movie> expectedMovies) {

        def emptyParametersMap = new LinkedHashMap<String, String>()
        when(mockMovieService.getAll(emptyParametersMap)).thenReturn(expectedMovies)
        mockMvc.perform(get("/movie").accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath('$', hasSize(2)))

                .andExpect(jsonPath('$[0].id', is(1)))
                .andExpect(jsonPath('$[0].nameRussian', is('Побег из Шоушенка')))
                .andExpect(jsonPath('$[0].nameNative', is('The Shawshank Redemption')))
                .andExpect(jsonPath('$[0].yearOfRelease', is('1994')))
                .andExpect(jsonPath('$[0].description', is('Успешный банкир Энди Дюфрейн обвинен в убийстве собственной жены и ее любовника. Оказавшись в тюрьме под названием Шоушенк, он сталкивается с жестокостью и беззаконием, царящими по обе стороны решетки. Каждый, кто попадает в эти стены, становится их рабом до конца жизни. Но Энди, вооруженный живым умом и доброй душой, отказывается мириться с приговором судьбы и начинает разрабатывать невероятно дерзкий план своего освобождения.')))
                .andExpect(jsonPath('$[0].rating', is(9.1d)))
                .andExpect(jsonPath('$[0].price', is(123.45d)))
                .andExpect(jsonPath('$[0].picturePath', is('shawshank173_173.jpg')))

                .andExpect(jsonPath('$[1].id', is(2)))
                .andExpect(jsonPath('$[1].nameRussian', is('Зеленая миля')))
                .andExpect(jsonPath('$[1].nameNative', is('The Green Mile')))
                .andExpect(jsonPath('$[1].yearOfRelease', is('1999')))
                .andExpect(jsonPath('$[1].description', is('Обвиненный в страшном преступлении, Джон Коффи оказывается в блоке смертников тюрьмы «Холодная гора». Вновь прибывший обладал поразительным ростом и был пугающе спокоен, что, впрочем, никак не влияло на отношение к нему начальника блока Пола Эджкомба, привыкшего исполнять приговор.')))
                .andExpect(jsonPath('$[1].rating', is(8.9d)))
                .andExpect(jsonPath('$[1].price', is(134.67d)))
                .andExpect(jsonPath('$[1].picturePath', is('green_mile173_173.jpg')))

        verify(mockMovieService, times(1)).getAll(emptyParametersMap)
        verifyNoMoreInteractions(mockMovieService)
    }

    @Test(dataProvider = "provideMoviesWithRequestParams", dataProviderClass = DataProviderController.class)
    void testGetAllMoviesWithSorting(List<Movie> expectedMovies, Map<String, String> requestParams) {

        when(mockMovieService.getAll(requestParams)).thenReturn(expectedMovies)
        mockMvc.perform(get("/movie")
                .param('price', 'asc')
                .param('name_russian', 'DESC')
                .param('RATING', 'desc')
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath('$', hasSize(2)))

                .andExpect(jsonPath('$[0].id', is(1)))
                .andExpect(jsonPath('$[0].nameRussian', is('Побег из Шоушенка')))
                .andExpect(jsonPath('$[0].nameNative', is('The Shawshank Redemption')))
                .andExpect(jsonPath('$[0].yearOfRelease', is('1994')))
                .andExpect(jsonPath('$[0].description', is('Успешный банкир Энди Дюфрейн обвинен в убийстве собственной жены и ее любовника. Оказавшись в тюрьме под названием Шоушенк, он сталкивается с жестокостью и беззаконием, царящими по обе стороны решетки. Каждый, кто попадает в эти стены, становится их рабом до конца жизни. Но Энди, вооруженный живым умом и доброй душой, отказывается мириться с приговором судьбы и начинает разрабатывать невероятно дерзкий план своего освобождения.')))
                .andExpect(jsonPath('$[0].rating', is(9.1d)))
                .andExpect(jsonPath('$[0].price', is(123.45d)))
                .andExpect(jsonPath('$[0].picturePath', is('shawshank173_173.jpg')))

                .andExpect(jsonPath('$[1].id', is(2)))
                .andExpect(jsonPath('$[1].nameRussian', is('Зеленая миля')))
                .andExpect(jsonPath('$[1].nameNative', is('The Green Mile')))
                .andExpect(jsonPath('$[1].yearOfRelease', is('1999')))
                .andExpect(jsonPath('$[1].description', is('Обвиненный в страшном преступлении, Джон Коффи оказывается в блоке смертников тюрьмы «Холодная гора». Вновь прибывший обладал поразительным ростом и был пугающе спокоен, что, впрочем, никак не влияло на отношение к нему начальника блока Пола Эджкомба, привыкшего исполнять приговор.')))
                .andExpect(jsonPath('$[1].rating', is(8.9d)))
                .andExpect(jsonPath('$[1].price', is(134.67d)))
                .andExpect(jsonPath('$[1].picturePath', is('green_mile173_173.jpg')))

        verify(mockMovieService, times(1)).getAll(requestParams)
        verifyNoMoreInteractions(mockMovieService)
    }


    @Test(dataProvider = "provideMoviesWithGenreAndCountry", dataProviderClass = DataProviderController.class)
    void testGetThreeRandomMovies(List<Movie> expectedMovies) {

        when(mockMovieService.getThreeRandomMovies()).thenReturn(expectedMovies)
        mockMvc.perform(get("/movie/random").accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath('$', hasSize(2)))

                .andExpect(jsonPath('$[0].id', is(1)))
                .andExpect(jsonPath('$[0].nameRussian', is('Побег из Шоушенка')))
                .andExpect(jsonPath('$[0].nameNative', is('The Shawshank Redemption')))
                .andExpect(jsonPath('$[0].yearOfRelease', is('1994')))
                .andExpect(jsonPath('$[0].description', is('Успешный банкир Энди Дюфрейн обвинен в убийстве собственной жены и ее любовника. Оказавшись в тюрьме под названием Шоушенк, он сталкивается с жестокостью и беззаконием, царящими по обе стороны решетки. Каждый, кто попадает в эти стены, становится их рабом до конца жизни. Но Энди, вооруженный живым умом и доброй душой, отказывается мириться с приговором судьбы и начинает разрабатывать невероятно дерзкий план своего освобождения.')))
                .andExpect(jsonPath('$[0].rating', is(9.1d)))
                .andExpect(jsonPath('$[0].price', is(123.45d)))
                .andExpect(jsonPath('$[0].picturePath', is('shawshank173_173.jpg')))
                .andExpect(jsonPath('$[0].genres[0].id', is(1)))
                .andExpect(jsonPath('$[0].genres[0].name', is('драма')))
                .andExpect(jsonPath('$[0].genres[1].id', is(2)))
                .andExpect(jsonPath('$[0].genres[1].name', is('криминал')))
                .andExpect(jsonPath('$[0].countries[0].id', is(1)))
                .andExpect(jsonPath('$[0].countries[0].name', is('США')))
                .andExpect(jsonPath('$[0].countries[1].id', is(2)))
                .andExpect(jsonPath('$[0].countries[1].name', is('Франция')))

                .andExpect(jsonPath('$[1].id', is(2)))
                .andExpect(jsonPath('$[1].nameRussian', is('Зеленая миля')))
                .andExpect(jsonPath('$[1].nameNative', is('The Green Mile')))
                .andExpect(jsonPath('$[1].yearOfRelease', is('1999')))
                .andExpect(jsonPath('$[1].description', is('Обвиненный в страшном преступлении, Джон Коффи оказывается в блоке смертников тюрьмы «Холодная гора». Вновь прибывший обладал поразительным ростом и был пугающе спокоен, что, впрочем, никак не влияло на отношение к нему начальника блока Пола Эджкомба, привыкшего исполнять приговор.')))
                .andExpect(jsonPath('$[1].rating', is(8.9d)))
                .andExpect(jsonPath('$[1].price', is(134.67d)))
                .andExpect(jsonPath('$[1].picturePath', is('green_mile173_173.jpg')))
                .andExpect(jsonPath('$[1].genres[0].id', is(5)))
                .andExpect(jsonPath('$[1].genres[0].name', is('мелодрама')))
                .andExpect(jsonPath('$[1].countries[0].id', is(3)))
                .andExpect(jsonPath('$[1].countries[0].name', is('Великобритания')))

        verify(mockMovieService, times(1)).getThreeRandomMovies()
        verifyNoMoreInteractions(mockMovieService)
    }

    @Test(dataProvider = "provideMovies", dataProviderClass = DataProviderController.class)
    void testGetByGenres(List<Movie> expectedMovies) {
        def emptyParametersMap = new LinkedHashMap<String, String>()

        when(mockMovieService.getByGenre(3, emptyParametersMap)).thenReturn(expectedMovies)
        mockMvc.perform(get("/movie/genre/{genreId}", 3).accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath('$', hasSize(2)))

                .andExpect(jsonPath('$[0].id', is(1)))
                .andExpect(jsonPath('$[0].nameRussian', is('Побег из Шоушенка')))
                .andExpect(jsonPath('$[0].nameNative', is('The Shawshank Redemption')))
                .andExpect(jsonPath('$[0].yearOfRelease', is('1994')))
                .andExpect(jsonPath('$[0].description', is('Успешный банкир Энди Дюфрейн обвинен в убийстве собственной жены и ее любовника. Оказавшись в тюрьме под названием Шоушенк, он сталкивается с жестокостью и беззаконием, царящими по обе стороны решетки. Каждый, кто попадает в эти стены, становится их рабом до конца жизни. Но Энди, вооруженный живым умом и доброй душой, отказывается мириться с приговором судьбы и начинает разрабатывать невероятно дерзкий план своего освобождения.')))
                .andExpect(jsonPath('$[0].rating', is(9.1d)))
                .andExpect(jsonPath('$[0].price', is(123.45d)))
                .andExpect(jsonPath('$[0].picturePath', is('shawshank173_173.jpg')))

                .andExpect(jsonPath('$[1].id', is(2)))
                .andExpect(jsonPath('$[1].nameRussian', is('Зеленая миля')))
                .andExpect(jsonPath('$[1].nameNative', is('The Green Mile')))
                .andExpect(jsonPath('$[1].yearOfRelease', is('1999')))
                .andExpect(jsonPath('$[1].description', is('Обвиненный в страшном преступлении, Джон Коффи оказывается в блоке смертников тюрьмы «Холодная гора». Вновь прибывший обладал поразительным ростом и был пугающе спокоен, что, впрочем, никак не влияло на отношение к нему начальника блока Пола Эджкомба, привыкшего исполнять приговор.')))
                .andExpect(jsonPath('$[1].rating', is(8.9d)))
                .andExpect(jsonPath('$[1].price', is(134.67d)))
                .andExpect(jsonPath('$[1].picturePath', is('green_mile173_173.jpg')))

        verify(mockMovieService, times(1)).getByGenre(3, emptyParametersMap)
        verifyNoMoreInteractions(mockMovieService)
    }

    @Test(dataProvider = "provideMoviesWithRequestParams", dataProviderClass = DataProviderController.class)
    void testGetByGenresWithSorting(List<Movie> expectedMovies, Map<String, String> requestParams) {

        when(mockMovieService.getByGenre(3, requestParams)).thenReturn(expectedMovies)
        mockMvc.perform(get("/movie/genre/{genreId}", 3)
                .param('price', 'asc')
                .param('name_russian', 'DESC')
                .param('RATING', 'desc')
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath('$', hasSize(2)))

                .andExpect(jsonPath('$[0].id', is(1)))
                .andExpect(jsonPath('$[0].nameRussian', is('Побег из Шоушенка')))
                .andExpect(jsonPath('$[0].nameNative', is('The Shawshank Redemption')))
                .andExpect(jsonPath('$[0].yearOfRelease', is('1994')))
                .andExpect(jsonPath('$[0].description', is('Успешный банкир Энди Дюфрейн обвинен в убийстве собственной жены и ее любовника. Оказавшись в тюрьме под названием Шоушенк, он сталкивается с жестокостью и беззаконием, царящими по обе стороны решетки. Каждый, кто попадает в эти стены, становится их рабом до конца жизни. Но Энди, вооруженный живым умом и доброй душой, отказывается мириться с приговором судьбы и начинает разрабатывать невероятно дерзкий план своего освобождения.')))
                .andExpect(jsonPath('$[0].rating', is(9.1d)))
                .andExpect(jsonPath('$[0].price', is(123.45d)))
                .andExpect(jsonPath('$[0].picturePath', is('shawshank173_173.jpg')))

                .andExpect(jsonPath('$[1].id', is(2)))
                .andExpect(jsonPath('$[1].nameRussian', is('Зеленая миля')))
                .andExpect(jsonPath('$[1].nameNative', is('The Green Mile')))
                .andExpect(jsonPath('$[1].yearOfRelease', is('1999')))
                .andExpect(jsonPath('$[1].description', is('Обвиненный в страшном преступлении, Джон Коффи оказывается в блоке смертников тюрьмы «Холодная гора». Вновь прибывший обладал поразительным ростом и был пугающе спокоен, что, впрочем, никак не влияло на отношение к нему начальника блока Пола Эджкомба, привыкшего исполнять приговор.')))
                .andExpect(jsonPath('$[1].rating', is(8.9d)))
                .andExpect(jsonPath('$[1].price', is(134.67d)))
                .andExpect(jsonPath('$[1].picturePath', is('green_mile173_173.jpg')))

        verify(mockMovieService, times(1)).getByGenre(3, requestParams)
        verifyNoMoreInteractions(mockMovieService)
    }

    @Test(dataProvider = "provideMovie", dataProviderClass = DataProviderController.class)
    void testGetById(Movie expectedMovie) {

        when(mockMovieService.getById(1)).thenReturn(expectedMovie)
        mockMvc.perform(get("/movie/{movieId}", 1).accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())

                .andExpect(jsonPath('$.id', is(expectedMovie.id)))
                .andExpect(jsonPath('$.nameRussian', is(expectedMovie.nameRussian)))
                .andExpect(jsonPath('$.nameNative', is(expectedMovie.nameNative)))
                .andExpect(jsonPath('$.yearOfRelease', is('1994')))
                .andExpect(jsonPath('$.description', is(expectedMovie.description)))
                .andExpect(jsonPath('$.rating', is(expectedMovie.rating)))
                .andExpect(jsonPath('$.price', is(expectedMovie.price)))
                .andExpect(jsonPath('$.picturePath', is(expectedMovie.picturePath)))
                .andExpect(jsonPath('$.genres[0].id', is(expectedMovie.genres.get(0).id)))
                .andExpect(jsonPath('$.genres[0].name', is(expectedMovie.genres.get(0).name)))
                .andExpect(jsonPath('$.genres[1].id', is(expectedMovie.genres.get(1).id)))
                .andExpect(jsonPath('$.genres[1].name', is(expectedMovie.genres.get(1).name)))
                .andExpect(jsonPath('$.countries[0].id', is(expectedMovie.countries.get(0).id)))
                .andExpect(jsonPath('$.countries[0].name', is(expectedMovie.countries.get(0).name)))
                .andExpect(jsonPath('$.countries[1].id', is(expectedMovie.countries.get(1).id)))
                .andExpect(jsonPath('$.countries[1].name', is(expectedMovie.countries.get(1).name)))
                .andExpect(jsonPath('$.reviews[0].id', is(expectedMovie.reviews.get(0).getId().intValue())))
                .andExpect(jsonPath('$.reviews[0].description', is(expectedMovie.reviews.get(0).description)))
                .andExpect(jsonPath('$.reviews[0].movie.id', is(expectedMovie.reviews.get(0).movie.id)))
                .andExpect(jsonPath('$.reviews[0].user.id', is(expectedMovie.reviews.get(0).user.id)))
                .andExpect(jsonPath('$.reviews[1].id', is(expectedMovie.reviews.get(1).getId().intValue())))
                .andExpect(jsonPath('$.reviews[1].description', is(expectedMovie.reviews.get(1).description)))
                .andExpect(jsonPath('$.reviews[1].movie.id', is(expectedMovie.reviews.get(1).movie.id)))
                .andExpect(jsonPath('$.reviews[1].user.id', is(expectedMovie.reviews.get(1).user.id)))

        verify(mockMovieService, times(1)).getById(1)
        verifyNoMoreInteractions(mockMovieService)
    }

    @Test(dataProvider = "provideMovie", dataProviderClass = DataProviderController.class)
    void testGetByIdWithCurrency(Movie expectedMovie) {

        when(rateReader.currentRates).thenReturn([new RateDto(cc: 'USD', rate: 26.931378)])

        when(mockMovieService.getById(1)).thenReturn(expectedMovie)
        mockMvc.perform(get("/movie/{movieId}", 1)
                .param('currency', 'USD')
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())

                .andExpect(jsonPath('$.id', is(expectedMovie.id)))
                .andExpect(jsonPath('$.nameRussian', is(expectedMovie.nameRussian)))
                .andExpect(jsonPath('$.nameNative', is(expectedMovie.nameNative)))
                .andExpect(jsonPath('$.yearOfRelease', is('1994')))
                .andExpect(jsonPath('$.description', is(expectedMovie.description)))
                .andExpect(jsonPath('$.rating', is(expectedMovie.rating)))
                .andExpect(jsonPath('$.price', is(4.58d)))
                .andExpect(jsonPath('$.picturePath', is(expectedMovie.picturePath)))
                .andExpect(jsonPath('$.genres[0].id', is(expectedMovie.genres.get(0).id)))
                .andExpect(jsonPath('$.genres[0].name', is(expectedMovie.genres.get(0).name)))
                .andExpect(jsonPath('$.genres[1].id', is(expectedMovie.genres.get(1).id)))
                .andExpect(jsonPath('$.genres[1].name', is(expectedMovie.genres.get(1).name)))
                .andExpect(jsonPath('$.countries[0].id', is(expectedMovie.countries.get(0).id)))
                .andExpect(jsonPath('$.countries[0].name', is(expectedMovie.countries.get(0).name)))
                .andExpect(jsonPath('$.countries[1].id', is(expectedMovie.countries.get(1).id)))
                .andExpect(jsonPath('$.countries[1].name', is(expectedMovie.countries.get(1).name)))
                .andExpect(jsonPath('$.reviews[0].id', is(expectedMovie.reviews.get(0).getId().intValue())))
                .andExpect(jsonPath('$.reviews[0].description', is(expectedMovie.reviews.get(0).description)))
                .andExpect(jsonPath('$.reviews[0].movie.id', is(expectedMovie.reviews.get(0).movie.id)))
                .andExpect(jsonPath('$.reviews[0].user.id', is(expectedMovie.reviews.get(0).user.id)))
                .andExpect(jsonPath('$.reviews[1].id', is(expectedMovie.reviews.get(1).getId().intValue())))
                .andExpect(jsonPath('$.reviews[1].description', is(expectedMovie.reviews.get(1).description)))
                .andExpect(jsonPath('$.reviews[1].movie.id', is(expectedMovie.reviews.get(1).movie.id)))
                .andExpect(jsonPath('$.reviews[1].user.id', is(expectedMovie.reviews.get(1).user.id)))

        verify(mockMovieService, times(2)).getById(1)
        verifyNoMoreInteractions(mockMovieService)
    }
}
