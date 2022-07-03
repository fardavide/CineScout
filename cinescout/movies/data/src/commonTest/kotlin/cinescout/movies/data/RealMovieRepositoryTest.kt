package cinescout.movies.data

import cinescout.movies.domain.model.Rating
import cinescout.movies.domain.testdata.MovieTestData.Inception
import io.mockk.coVerifySequence
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import kotlin.test.Test

internal class RealMovieRepositoryTest {

    private val localMovieDataSource: LocalMovieDataSource = mockk(relaxUnitFun = true)
    private val repository = RealMovieRepository(localMovieDataSource = localMovieDataSource)

    @Test
    fun `add to watchlist inserts movie and watchlist`() = runTest {
        // given
        val movie = Inception

        // when
        repository.addToWatchlist(movie)

        // then
        coVerifySequence {
            localMovieDataSource.insert(movie)
            localMovieDataSource.insertWatchlist(movie)
        }
    }

    @Test
    fun `rate movie inserts movie and rating`() = runTest {
        // given
        val movie = Inception
        Rating.of(8).tap { rating ->

            // when
            repository.rate(Inception, rating)

            // then
            coVerifySequence {
                localMovieDataSource.insert(movie)
                localMovieDataSource.insertRating(movie, rating)
            }
        }
    }
}
