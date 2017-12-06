package com.miskevich.movieland.dao.jdbc

import com.miskevich.movieland.dao.jdbc.mapper.MovieRatingRowMapper
import com.miskevich.movieland.dao.jdbc.provider.SQLDataProvider
import com.miskevich.movieland.entity.Movie
import com.miskevich.movieland.model.MovieRating
import com.miskevich.movieland.model.SortingField
import com.miskevich.movieland.model.SortingType
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.jdbc.core.namedparam.EmptySqlParameterSource
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate
import org.springframework.test.context.ContextConfiguration
import org.springframework.test.context.testng.AbstractTestNGSpringContextTests
import org.testng.annotations.Test

import static org.testng.Assert.assertEquals
import static org.testng.Assert.assertNotNull

@ContextConfiguration(locations = "classpath:spring/jdbc-context.xml")
class JdbcMovieDaoITest extends AbstractTestNGSpringContextTests {

    @Autowired
    private JdbcMovieDao jdbcMovieDao
    @Autowired
    private NamedParameterJdbcTemplate namedParameterJdbcTemplate
    private static final MovieRatingRowMapper MOVIE_RATING_ROW_MAPPER = new MovieRatingRowMapper()

    @Test
    void testGetAll() {
        def movies = jdbcMovieDao.getAll(new LinkedHashMap<>())
        for (Movie movie : movies) {
            assertNotNull(movie.getNameRussian())
            assertNotNull(movie.getNameNative())
            assertNotNull(movie.getYearOfRelease())
            assertNotNull(movie.getDescription())
            assertNotNull(movie.getPicturePath())
        }
    }

    @Test(dataProvider = "provideParamsSQL", dataProviderClass = SQLDataProvider.class)
    void testGetAllWithSorting(Map<SortingField, SortingType> paramsMap) {
        def movies = jdbcMovieDao.getAll(paramsMap)
        for (Movie movie : movies) {
            assertNotNull(movie.getNameRussian())
            assertNotNull(movie.getNameNative())
            assertNotNull(movie.getYearOfRelease())
            assertNotNull(movie.getDescription())
            assertNotNull(movie.getPicturePath())
        }
    }

    @Test
    void testGetThreeRandomMovies() {
        def movies = jdbcMovieDao.getThreeRandomMovies()
        for (Movie movie : movies) {
            assertNotNull(movie.getNameRussian())
            assertNotNull(movie.getNameNative())
            assertNotNull(movie.getYearOfRelease())
            assertNotNull(movie.getDescription())
            assertNotNull(movie.getPicturePath())
        }
    }

    @Test
    void testGetByGenre() {
        def movies = jdbcMovieDao.getByGenre(3, new LinkedHashMap<SortingField, SortingType>())
        for (Movie movie : movies) {
            assertNotNull(movie.getNameRussian())
            assertNotNull(movie.getNameNative())
            assertNotNull(movie.getYearOfRelease())
            assertNotNull(movie.getDescription())
            assertNotNull(movie.getPicturePath())
        }
    }

    @Test(dataProvider = "provideParamsSQL", dataProviderClass = SQLDataProvider.class)
    void testGetByGenreWithSorting(Map<SortingField, SortingType> paramsMap) {
        def movies = jdbcMovieDao.getByGenre(3, paramsMap)
        for (Movie movie : movies) {
            assertNotNull(movie.getNameRussian())
            assertNotNull(movie.getNameNative())
            assertNotNull(movie.getYearOfRelease())
            assertNotNull(movie.getDescription())
            assertNotNull(movie.getPicturePath())
        }
    }

    @Test
    void testGetById() {
        def movie = jdbcMovieDao.getById(3)
        assertNotNull(movie.getNameRussian())
        assertNotNull(movie.getNameNative())
        assertNotNull(movie.getYearOfRelease())
        assertNotNull(movie.getDescription())
        assertNotNull(movie.getPicturePath())
    }

    @Test(dataProvider = 'provideMovieSave', dataProviderClass = SQLDataProvider.class)
    void testSave(movieExpected) {
        def movie = jdbcMovieDao.persist(movieExpected)
        assertEquals(movie.nameRussian, movieExpected.nameRussian)
        assertEquals(movie.nameNative, movieExpected.nameNative)
        assertEquals(movie.yearOfRelease, movieExpected.yearOfRelease)
        assertEquals(movie.description, movieExpected.description)
        assertEquals(movie.picturePath, movieExpected.picturePath)
        assertEquals(movie.rating, movieExpected.rating)
        assertEquals(movie.price, movieExpected.price)
        assertEquals(movie.genres[0].id, movieExpected.genres[0].id)
        assertEquals(movie.genres[0].name, movieExpected.genres[0].name)
        assertEquals(movie.genres[1].id, movieExpected.genres[1].id)
        assertEquals(movie.genres[1].name, movieExpected.genres[1].name)
        assertEquals(movie.countries[0].id, movieExpected.countries[0].id)
        assertEquals(movie.countries[0].name, movieExpected.countries[0].name)
        assertEquals(movie.countries[1].id, movieExpected.countries[1].id)
        assertEquals(movie.countries[1].name, movieExpected.countries[1].name)
        assertEquals(movie.reviews[0].id, movieExpected.reviews[0].id)
        assertEquals(movie.reviews[0].text, movieExpected.reviews[0].text)
        assertEquals(movie.reviews[0].movie.id, movieExpected.reviews[0].movie.id)
        assertEquals(movie.reviews[0].user.id, movieExpected.reviews[0].user.id)
        assertEquals(movie.reviews[1].id, movieExpected.reviews[1].id)
        assertEquals(movie.reviews[1].text, movieExpected.reviews[1].text)
        assertEquals(movie.reviews[1].movie.id, movieExpected.reviews[1].movie.id)
        assertEquals(movie.reviews[1].user.id, movieExpected.reviews[1].user.id)
    }

    @Test(dataProvider = 'provideMovieForEnrichmentSave', dataProviderClass = SQLDataProvider.class)
    void testUpdate(movieExpected) {
        def movie = jdbcMovieDao.update(movieExpected)
        assertEquals(movie.id, movieExpected.id)
        assertEquals(movie.nameRussian, movieExpected.nameRussian)
        assertEquals(movie.nameNative, movieExpected.nameNative)
        assertEquals(movie.yearOfRelease, movieExpected.yearOfRelease)
        assertEquals(movie.description, movieExpected.description)
        assertEquals(movie.picturePath, movieExpected.picturePath)
        assertEquals(movie.rating, movieExpected.rating)
        assertEquals(movie.price, movieExpected.price)
        assertEquals(movie.genres[0].id, movieExpected.genres[0].id)
        assertEquals(movie.genres[0].name, movieExpected.genres[0].name)
        assertEquals(movie.genres[1].id, movieExpected.genres[1].id)
        assertEquals(movie.genres[1].name, movieExpected.genres[1].name)
        assertEquals(movie.countries[0].id, movieExpected.countries[0].id)
        assertEquals(movie.countries[0].name, movieExpected.countries[0].name)
        assertEquals(movie.countries[1].id, movieExpected.countries[1].id)
        assertEquals(movie.countries[1].name, movieExpected.countries[1].name)
        assertEquals(movie.reviews[0].id, movieExpected.reviews[0].id)
        assertEquals(movie.reviews[0].text, movieExpected.reviews[0].text)
        assertEquals(movie.reviews[0].movie.id, movieExpected.reviews[0].movie.id)
        assertEquals(movie.reviews[0].user.id, movieExpected.reviews[0].user.id)
        assertEquals(movie.reviews[1].id, movieExpected.reviews[1].id)
        assertEquals(movie.reviews[1].text, movieExpected.reviews[1].text)
        assertEquals(movie.reviews[1].movie.id, movieExpected.reviews[1].movie.id)
        assertEquals(movie.reviews[1].user.id, movieExpected.reviews[1].user.id)
    }

    @Test(dataProvider = 'movieRating', dataProviderClass = SQLDataProvider.class)
    void testRate(movieRating, String removeMovieRatingSQL, MapSqlParameterSource parameters, String getMovieRatingSQL){
        namedParameterJdbcTemplate.update(removeMovieRatingSQL, parameters)
        jdbcMovieDao.rate(movieRating)
        def actualRating = namedParameterJdbcTemplate.queryForObject(getMovieRatingSQL, parameters, Double.class)
        assertEquals(actualRating, movieRating.rating)
    }

    @Test
    void testGetAllMovieWithRatings(){
        jdbcMovieDao.getAllMoviesWithRatings()
    }

    @Test(dataProvider = 'movieRatingUpdate', dataProviderClass = SQLDataProvider.class)
    void testPersistRatingsAndVotes(movieRatingExpected, String getMovieRatingSQLFirst, String getMovieRatingSQLSecond){
        jdbcMovieDao.persistRatingsAndVotes(movieRatingExpected)
        def movieRatingFirst = namedParameterJdbcTemplate.queryForObject(getMovieRatingSQLFirst, EmptySqlParameterSource.INSTANCE, MOVIE_RATING_ROW_MAPPER)
        assertEquals(movieRatingFirst.rating, movieRatingExpected.get(0).rating / movieRatingExpected.get(0).getVotes())
        assertEquals(movieRatingFirst.votes, movieRatingExpected.get(0).votes)

        def movieRatingSecond = namedParameterJdbcTemplate.queryForObject(getMovieRatingSQLSecond, EmptySqlParameterSource.INSTANCE, MOVIE_RATING_ROW_MAPPER)
        assertEquals(movieRatingSecond.rating, movieRatingExpected.get(1).rating / movieRatingExpected.get(1).getVotes())
        assertEquals(movieRatingSecond.votes, movieRatingExpected.get(1).votes)
    }
}
