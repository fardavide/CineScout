package cinescout.movies.domain.usecase

import app.cash.turbine.test
import arrow.core.right
import cinescout.movies.domain.MovieRepository
import cinescout.movies.domain.sample.MovieWithPersonalRatingSample
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import kotlinx.coroutines.test.runTest
import store.Paging
import store.builder.pagedStoreOf
import store.builder.toRemotePagedData
import kotlin.test.Test
import kotlin.test.assertEquals

class GetAllRatedMoviesTest {

    private val movieRepository: MovieRepository = mockk()
    private val getAllRatedMovies = GetAllRatedMovies(movieRepository = movieRepository)

    @Test
    fun `get all rated movies from repository`() = runTest {
        // given
        val moviesWithRating = listOf(
            MovieWithPersonalRatingSample.Inception,
            MovieWithPersonalRatingSample.TheWolfOfWallStreet
        )
        every { movieRepository.getAllRatedMovies(any()) } returns pagedStoreOf(moviesWithRating)
        val expected = moviesWithRating.toRemotePagedData(Paging.Page.Initial).right()

        // when
        getAllRatedMovies().test {

            // then
            assertEquals(expected, awaitItem())
            awaitComplete()
            verify { movieRepository.getAllRatedMovies(any()) }
        }
    }
}
