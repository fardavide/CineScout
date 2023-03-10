package cinescout.movies.domain.usecase

import app.cash.turbine.test
import arrow.core.right
import cinescout.movies.domain.MovieRepository
import cinescout.movies.domain.sample.MovieSample
import cinescout.store5.test.storeFlowOf
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import kotlinx.coroutines.test.runTest
import kotlin.test.Test
import kotlin.test.assertEquals

class GetAllWatchlistMoviesTest {

    private val movieRepository: MovieRepository = mockk()
    private val getAllWatchlistMovies = RealGetAllWatchlistMovies(movieRepository = movieRepository)

    @Test
    fun `get all watchlist movies from repository`() = runTest {
        // given
        val movies = listOf(
            MovieSample.Inception,
            MovieSample.TheWolfOfWallStreet
        )
        every { movieRepository.getAllWatchlistMovies(any()) } returns storeFlowOf(movies)
        val expected = movies.right()

        // when
        getAllWatchlistMovies().test {

            // then
            assertEquals(expected, awaitItem().dataOrNull())
            awaitComplete()
            verify { movieRepository.getAllWatchlistMovies(any()) }
        }
    }
}
