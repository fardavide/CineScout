package cinescout.movies.domain.usecase

import app.cash.turbine.test
import arrow.core.right
import cinescout.movies.domain.sample.MovieSample
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import store.builder.emptyListStore
import store.builder.storeOf
import kotlin.test.Test
import kotlin.test.assertEquals

internal class GetIsMovieInWatchlistTest {

    private val getAllWatchlistMovies: GetAllWatchlistMovies = mockk {
        every { this@mockk(refresh = any()) } returns emptyListStore()
    }
    private val getIsMovieInWatchlist = GetIsMovieInWatchlist(getAllWatchlistMovies)

    @Test
    fun `get correct value for movie in watchlist`() = runTest {
        // given
        val movie = MovieSample.Inception
        every { getAllWatchlistMovies(refresh = any()) } returns storeOf(listOf(movie))

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
        every { getAllWatchlistMovies(refresh = any()) } returns emptyListStore()

        // when
        getIsMovieInWatchlist(movie.tmdbId).test {

            // then
            assertEquals(false.right(), awaitItem())
            awaitComplete()
        }
    }
}

