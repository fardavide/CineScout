package cinescout.movies.domain.usecase

import cinescout.movies.domain.model.Rating
import cinescout.movies.domain.testdata.MovieTestData.Inception
import kotlinx.coroutines.test.runTest
import kotlin.test.Test

internal class RateMovieTest {

    private val rateMovie = RateMovie()

    @Test
    fun `does nothing`() = runTest {
        Rating.of(8).tap { rating ->
            rateMovie(Inception, rating)
        }
    }
}
