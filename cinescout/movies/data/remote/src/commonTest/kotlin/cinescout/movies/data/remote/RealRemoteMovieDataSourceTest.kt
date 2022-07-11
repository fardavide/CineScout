package cinescout.movies.data.remote

import arrow.core.right
import cinescout.movies.domain.model.Rating
import cinescout.movies.domain.testdata.MovieTestData
import cinescout.movies.domain.testdata.TmdbMovieIdTestData
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import kotlin.test.Test
import kotlin.test.assertEquals

internal class RealRemoteMovieDataSourceTest {

    private val tmdbSource: TmdbRemoteMovieDataSource = mockk(relaxUnitFun = true) {
        coEvery { postRating(any(), any()) } returns Unit.right()
    }
    private val traktSource: TraktRemoteMovieDataSource = mockk(relaxUnitFun = true)
    private val remoteMovieDataSource = RealRemoteMovieDataSource(tmdbSource = tmdbSource, traktSource = traktSource)

    @Test
    fun `get movie returns the right movie from Tmdb`() = runTest {
        // given
        val expected = MovieTestData.Inception.right()
        val movieId = TmdbMovieIdTestData.Inception
        coEvery { tmdbSource.getMovie(movieId) } returns expected

        // when
        val result = remoteMovieDataSource.getMovie(movieId)

        // then
        assertEquals(expected, result)
    }

    @Test
    fun `post rating posts to tmdb and trakt`() = runTest {
        // given
        val movie = MovieTestData.Inception

        // when
        Rating.of(8).tap { rating ->
            val result = remoteMovieDataSource.postRating(movie, rating)

            // then
            assertEquals(Unit.right(), result)
            coVerify { tmdbSource.postRating(movie, rating) }
            coVerify { traktSource.postRating(movie, rating) }
        }
    }

    @Test
    fun `post watchlist posts to tmdb and trakt`() = runTest {
        // given
        val movie = MovieTestData.Inception

        // when
        remoteMovieDataSource.postWatchlist(movie)

        // then
        coVerify { tmdbSource.postWatchlist(movie) }
        coVerify { traktSource.postWatchlist(movie) }
    }
}
