package cinescout.movies.data

import cinescout.movies.domain.model.Rating
import cinescout.movies.domain.testdata.MovieTestData.Inception
import kotlinx.coroutines.test.runTest
import kotlin.test.Test

internal class RealMovieRepositoryTest {

    private val repository = RealMovieRepository()

    @Test
    fun `add to watchlist does nothing`() = runTest {
        repository.addToWatchlist(Inception)
    }

    @Test
    fun `rate movie does nothing`() = runTest {
        Rating.of(8).tap { rating ->
            repository.rate(Inception, rating)
        }
    }
}
