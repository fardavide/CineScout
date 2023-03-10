package cinescout.movies.domain.usecase

import app.cash.turbine.test
import arrow.core.right
import cinescout.movies.domain.MovieRepository
import cinescout.movies.domain.sample.MovieWithPersonalRatingSample
import cinescout.store5.test.storeFlowOf
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import kotlinx.coroutines.test.runTest
import kotlin.test.Test
import kotlin.test.assertEquals

class GetAllRatedMoviesTest {

    private val movieRepository: MovieRepository = mockk()
    private val getAllRatedMovies = RealGetAllRatedMovies(movieRepository = movieRepository)

    @Test
    fun `get all rated movies from repository`() = runTest {
        // given
        val moviesWithRating = listOf(
            MovieWithPersonalRatingSample.Inception,
            MovieWithPersonalRatingSample.TheWolfOfWallStreet
        )
        every { movieRepository.getAllRatedMovies(any()) } returns storeFlowOf(moviesWithRating)
        val expected = moviesWithRating.right()

        // when
        getAllRatedMovies().test {

            // then
            assertEquals(expected, awaitItem().dataOrNull())
            awaitComplete()
            verify { movieRepository.getAllRatedMovies(any()) }
        }
    }
}
