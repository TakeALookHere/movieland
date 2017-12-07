package com.miskevich.movieland.service.cache

import com.miskevich.movieland.entity.Movie
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.test.context.ContextConfiguration
import org.springframework.test.context.testng.AbstractTestNGSpringContextTests
import org.testng.annotations.Test

import static org.testng.Assert.assertFalse

@ContextConfiguration(locations = "classpath:spring/service-context.xml")
class MovieCachePTest extends AbstractTestNGSpringContextTests {

    @Autowired
    private MovieCache movieCache
    private List<String> loadMemoryList = new ArrayList<>()

    @Test
    void getMovieFromCache() {
        //-Xms19m -Xmx19m
        for (int i = 1; i < 1_500; i++) {
            def movie = new Movie()
            movie.setId(i)
            movieCache.add(movie)
        }
        loadMemory()

        Optional<Movie> optional = movieCache.get(1)
        assertFalse(optional.isPresent())
    }

    private void loadMemory() {
        for (int i = 0; i < 90_000; i++) {
            loadMemoryList.add(i + "")
        }
    }
}
