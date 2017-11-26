package com.miskevich.movieland.service.cache

import com.miskevich.movieland.entity.Movie
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.test.context.ContextConfiguration
import org.springframework.test.context.testng.AbstractTestNGSpringContextTests
import org.testng.annotations.Test

import static org.testng.Assert.assertFalse

@ContextConfiguration(locations = "classpath:spring/service-context.xml")
class MovieCacheTest extends AbstractTestNGSpringContextTests {

    @Autowired
    private MovieCache movieCache
    List<String> loadMemoryList = new ArrayList<>()

    @Test
    void getMovieFromCache() {
        //-Xms20m -Xmx20m
        for (int i = 1; i < 65; i++) {
            def movie = new Movie()
            movie.setId(i)
            movieCache.put(i, movie)
        }
        loadMemory()
        System.gc()

        Optional<Movie> optional = movieCache.get(1)
        println optional.get()
        assertFalse(optional.isPresent())
    }

    private void loadMemory() {
        for (int i = 0; i < 120_000; i++) {
            loadMemoryList.add(i + "")
        }
    }
}
