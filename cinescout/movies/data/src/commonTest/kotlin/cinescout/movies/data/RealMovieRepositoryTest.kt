package cinescout.movies.data

import arrow.core.right
import cinescout.movies.domain.model.Rating
import cinescout.movies.domain.testdata.MovieTestData
import io.mockk.coEvery
import io.mockk.coVerifySequence
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import kotlin.test.Test
import kotlin.test.assertEquals

internal class RealMovieRepositoryTest {

    private val localMovieDataSource: LocalMovieDataSource = mockk(relaxUnitFun = true)
    private val remoteMovieDataSource: RemoteMovieDataSource = mockk(relaxUnitFun = true) {
        coEvery { postRating(any(), any()) } returns Unit.right()
    }
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
            val result = repository.rate(MovieTestData.Inception, rating)

            // then
            assertEquals(Unit.right(), result)
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
