package cinescout.movies.domain.usecase

import app.cash.turbine.test
import arrow.core.right
import cinescout.movies.domain.sample.MovieSample
import cinescout.store5.test.storeFlowOf
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import kotlin.test.Test
import kotlin.test.assertEquals

internal class GetIsMovieInWatchlistTest {

    private val getAllWatchlistMovies: GetAllWatchlistMovies = mockk {
        every { this@mockk(refresh = any()) } returns storeFlowOf(emptyList())
    }
    private val getIsMovieInWatchlist = GetIsMovieInWatchlist(getAllWatchlistMovies)

    @Test
    fun `get correct value for movie in watchlist`() = runTest {
        // given
        val movie = MovieSample.Inception
        every { getAllWatchlistMovies(refresh = any()) } returns storeFlowOf(listOf(movie))

        // when
        getIsMovieInWatchlist(movie.tmdbId).test {

            // then
            assertEquals(true.right(), awaitItem())
            awaitComplete()
        }
    }

    @Test
    fun `get none for a movie that has not been rated`() = runTest {
        // given
        val movie = MovieSample.Inception
        every { getAllWatchlistMovies(refresh = any()) } returns storeFlowOf(emptyList())

        // when
        getIsMovieInWatchlist(movie.tmdbId).test {

            // then
            assertEquals(false.right(), awaitItem())
            awaitComplete()
        }
    }
}

