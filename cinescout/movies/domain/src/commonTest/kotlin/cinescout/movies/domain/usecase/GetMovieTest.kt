package cinescout.movies.domain.usecase

import app.cash.turbine.test
import arrow.core.right
import cinescout.movies.domain.MovieRepository
import cinescout.movies.domain.testdata.MovieTestData
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import kotlin.test.Test
import kotlin.test.assertEquals

class GetMovieTest {

    private val movieRepository: MovieRepository = mockk()
    private val getMovie = GetMovie(movieRepository = movieRepository)

    @Test
    fun `get movie from repository`() = runTest {
        // given
        val movie = MovieTestData.Inception
        every { movieRepository.getMovie(movie.tmdbId) } returns flowOf(movie.right())
        val expected = movie.right()

        // when
        getMovie(movie.tmdbId).test {

            // then
            assertEquals(expected, awaitItem())
            awaitComplete()
            verify { movieRepository.getMovie(movie.tmdbId) }
        }
    }
}
