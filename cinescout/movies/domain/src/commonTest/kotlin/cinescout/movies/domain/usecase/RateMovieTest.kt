package cinescout.movies.domain.usecase

import arrow.core.right
import cinescout.movies.domain.MovieRepository
import cinescout.movies.domain.model.Rating
import cinescout.movies.domain.testdata.MovieTestData.Inception
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import kotlin.test.Test
import kotlin.test.assertEquals

internal class RateMovieTest {

    private val movieRepository: MovieRepository = mockk {
        coEvery { rate(any(), any()) } returns Unit.right()
    }
    private val rateMovie = RateMovie(movieRepository)

    @Test
    fun `does call repository`() = runTest {
        // given
        val movie = Inception
        Rating.of(8).tap { rating ->

            // when
            val result = rateMovie(movie, rating)

            // then
            assertEquals(Unit.right(), result)
            coVerify { movieRepository.rate(movie, rating) }
        }
    }
}
