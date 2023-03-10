package cinescout.movies.domain.usecase

import app.cash.turbine.test
import arrow.core.right
import cinescout.movies.domain.MovieRepository
import cinescout.movies.domain.sample.MovieWithDetailsSample
import cinescout.store5.test.storeFlowOf
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import kotlinx.coroutines.test.runTest
import kotlin.test.Test
import kotlin.test.assertEquals

class GetMovieDetailsTest {

    private val movieRepository: MovieRepository = mockk()
    private val getMovieDetails = GetMovieDetails(movieRepository = movieRepository)

    @Test
    fun `get movie from repository`() = runTest {
        // given
        val movie = MovieWithDetailsSample.Inception
        every { movieRepository.getMovieDetails(movie.movie.tmdbId, any<Boolean>()) } returns storeFlowOf(movie)
        val expected = movie.right()

        // when
        getMovieDetails(movie.movie.tmdbId).test {

            // then
            assertEquals(expected, awaitItem().dataOrNull())
            awaitComplete()
            verify { movieRepository.getMovieDetails(movie.movie.tmdbId, any<Boolean>()) }
        }
    }
}
