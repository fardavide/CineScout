package cinescout.movies.domain.usecase

import app.cash.turbine.test
import arrow.core.right
import cinescout.movies.domain.MovieRepository
import cinescout.movies.domain.sample.MovieSample
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import kotlinx.coroutines.test.runTest
import store.Paging
import store.builder.pagedStoreOf
import store.builder.toPagedData
import kotlin.test.Test
import kotlin.test.assertEquals

class GetAllWatchlistMoviesTest {

    private val movieRepository: MovieRepository = mockk()
    private val getAllWatchlistMovies = GetAllWatchlistMovies(movieRepository = movieRepository)

    @Test
    fun `get all watchlist movies from repository`() = runTest {
        // given
        val movies = listOf(
            MovieSample.Inception,
            MovieSample.TheWolfOfWallStreet
        )
        every { movieRepository.getAllWatchlistMovies(any()) } returns pagedStoreOf(movies)
        val expected = movies.toPagedData(Paging.Page.Initial).right()

        // when
        getAllWatchlistMovies().test {

            // then
            assertEquals(expected, awaitItem())
            awaitComplete()
            verify { movieRepository.getAllWatchlistMovies(any()) }
        }
    }
}
