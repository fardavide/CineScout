package cinescout.movies.domain.usecase

import app.cash.turbine.test
import arrow.core.right
import cinescout.movies.domain.sample.MovieSample
import cinescout.movies.domain.store.FakeWatchlistMoviesStore
import kotlinx.coroutines.test.runTest
import kotlin.test.Test
import kotlin.test.assertEquals

class GetAllWatchlistMoviesTest {

    private val watchlistMoviesStore = FakeWatchlistMoviesStore(
        movies = listOf(
            MovieSample.Inception,
            MovieSample.TheWolfOfWallStreet
        )
    )
    private val getAllWatchlistMovies = RealGetAllWatchlistMovies(watchlistMoviesStore = watchlistMoviesStore)

    @Test
    fun `get all watchlist movies from repository`() = runTest {
        // given
        val movies = listOf(
            MovieSample.Inception,
            MovieSample.TheWolfOfWallStreet
        )
        val expected = movies.right()

        // when
        getAllWatchlistMovies().test {

            // then
            assertEquals(expected, awaitItem().dataOrNull())
            awaitComplete()
        }
    }
}
