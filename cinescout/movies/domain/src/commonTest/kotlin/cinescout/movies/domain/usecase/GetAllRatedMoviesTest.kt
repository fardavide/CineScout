package cinescout.movies.domain.usecase

import app.cash.turbine.test
import arrow.core.right
import cinescout.movies.domain.MovieRepository
import cinescout.movies.domain.testdata.MovieWithRatingTestData
import cinescout.store.Paging
import cinescout.store.pagedStoreOf
import cinescout.store.toPagedData
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import kotlinx.coroutines.test.runTest
import kotlin.test.Test
import kotlin.test.assertEquals

class GetAllRatedMoviesTest {

    private val movieRepository: MovieRepository = mockk()
    private val getAllRatedMovies = GetAllRatedMovies(movieRepository = movieRepository)

    @Test
    fun `get all rated movies from repository`() = runTest {
        // given
        val moviesWithRating = listOf(MovieWithRatingTestData.Inception, MovieWithRatingTestData.TheWolfOfWallStreet)
        every { movieRepository.getAllRatedMovies() } returns pagedStoreOf(moviesWithRating)
        val expected = moviesWithRating.toPagedData(Paging.Page(1, 1)).right()

        // when
        getAllRatedMovies().test {

            // then
            assertEquals(expected, awaitItem())
            awaitComplete()
            verify { movieRepository.getAllRatedMovies() }
        }
    }
}
