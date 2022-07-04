package cinescout.movies.data

import cinescout.movies.domain.model.Rating
import cinescout.movies.domain.testdata.MovieTestData
import io.mockk.coVerifySequence
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import kotlin.test.Test

internal class RealMovieRepositoryTest {

    private val localMovieDataSource: LocalMovieDataSource = mockk(relaxUnitFun = true)
    private val remoteMovieDataSource: RemoteMovieDataSource = mockk(relaxUnitFun = true)
    private val repository = RealMovieRepository(
        localMovieDataSource = localMovieDataSource,
        remoteMovieDataSource = remoteMovieDataSource
    )

    @Test
    fun `rate movie inserts movie and rating locally and post to remote`() = runTest {
        // given
        val movie = MovieTestData.Inception
        Rating.of(8).tap { rating ->

            // when
            repository.rate(MovieTestData.Inception, rating)

            // then
            coVerifySequence {
                localMovieDataSource.insert(movie)
                localMovieDataSource.insertRating(movie, rating)
                remoteMovieDataSource.postRating(movie, rating)
            }
        }
    }

    @Test
    fun `add to watchlist inserts movie and watchlist locally and post to remote`() = runTest {
        // given
        val movie = MovieTestData.Inception

        // when
        repository.addToWatchlist(movie)

        // then
        coVerifySequence {
            localMovieDataSource.insert(movie)
            localMovieDataSource.insertWatchlist(movie)
            remoteMovieDataSource.postWatchlist(movie)
        }
    }

}
