package cinescout.movies.domain.usecase

import app.cash.turbine.test
import arrow.core.right
import cinescout.movies.domain.sample.MovieWithDetailsSample
import cinescout.movies.domain.store.FakeMovieDetailsStore
import kotlinx.coroutines.test.runTest
import kotlin.test.Test
import kotlin.test.assertEquals

class GetMovieDetailsTest {

    private val movieDetailsStore = FakeMovieDetailsStore(listOf(MovieWithDetailsSample.Inception))
    private val getMovieDetails = GetMovieDetails(movieDetailsStore = movieDetailsStore)

    @Test
    fun `get movie from repository`() = runTest {
        // given
        val movie = MovieWithDetailsSample.Inception
        val expected = movie.right()

        // when
        getMovieDetails(movie.movie.tmdbId).test {

            // then
            assertEquals(expected, awaitItem().dataOrNull())
            awaitComplete()
        }
    }
}
