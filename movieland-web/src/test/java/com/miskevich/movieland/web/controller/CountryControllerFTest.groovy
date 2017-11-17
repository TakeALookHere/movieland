package com.miskevich.movieland.web.controller

import com.miskevich.movieland.entity.Country
import com.miskevich.movieland.service.ICountryService
import com.miskevich.movieland.web.controller.provider.ControllerDataProvider
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

class CountryControllerFTest {

    @Mock
    private ICountryService mockCountryService
    @InjectMocks
    private CountryController countryController
    private MockMvc mockMvc

    @BeforeTest
    private void setup() {
        MockitoAnnotations.initMocks(this)
        mockMvc = MockMvcBuilders.standaloneSetup(countryController).build()
    }

    @Test(dataProvider = "provideCountries", dataProviderClass = ControllerDataProvider.class)
    void testGetAll(List<Country> expectedCountries) {
        when(mockCountryService.getAll()).thenReturn(expectedCountries)
        mockMvc.perform(get("/country").accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath('$', hasSize(2)))

                .andExpect(jsonPath('$[0].id', is(1)))
                .andExpect(jsonPath('$[0].name', is('США')))

                .andExpect(jsonPath('$[1].id', is(2)))
                .andExpect(jsonPath('$[1].name', is('Франция')))

        verify(mockCountryService, times(1)).getAll()
        verifyNoMoreInteractions(mockCountryService)
    }
}
