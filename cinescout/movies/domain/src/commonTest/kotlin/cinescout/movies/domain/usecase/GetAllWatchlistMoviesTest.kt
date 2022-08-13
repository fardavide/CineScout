package cinescout.movies.domain.usecase

import app.cash.turbine.test
import arrow.core.right
import cinescout.movies.domain.MovieRepository
import cinescout.movies.domain.testdata.MovieTestData
import cinescout.store.Paging
import cinescout.store.dualSourcesPagedStoreOf
import cinescout.store.toPagedData
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import kotlinx.coroutines.test.runTest
import kotlin.test.Test
import kotlin.test.assertEquals

class GetAllWatchlistMoviesTest {

    private val movieRepository: MovieRepository = mockk()
    private val getAllWatchlistMovies = GetAllWatchlistMovies(movieRepository = movieRepository)

    @Test
    fun `get all watchlist movies from repository`() = runTest {
        // given
        val movies = listOf(
            MovieTestData.Inception,
            MovieTestData.TheWolfOfWallStreet
        )
        every { movieRepository.getAllWatchlistMovies() } returns dualSourcesPagedStoreOf(movies)
        val expected = movies.toPagedData(Paging.Page.DualSources.Initial).right()

        // when
        getAllWatchlistMovies().test {

            // then
            assertEquals(expected, awaitItem())
            awaitComplete()
            verify { movieRepository.getAllWatchlistMovies() }
        }
    }
}
