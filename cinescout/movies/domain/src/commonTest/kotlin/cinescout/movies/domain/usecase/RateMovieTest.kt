package cinescout.movies.domain.usecase

import cinescout.movies.domain.MovieRepository
import cinescout.movies.domain.model.Rating
import cinescout.movies.domain.testdata.MovieTestData.Inception
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import kotlin.test.Test

internal class RateMovieTest {

    private val movieRepository: MovieRepository = mockk(relaxUnitFun = true)
    private val rateMovie = RateMovie(movieRepository)

    @Test
    fun `does call repository`() = runTest {
        // given
        val movie = Inception
        Rating.of(8).tap { rating ->

            // when
            rateMovie(movie, rating)

            // then
            coVerify { movieRepository.rate(movie, rating) }
        }
    }
}
