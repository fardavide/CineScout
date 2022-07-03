package cinescout.movies.data

import cinescout.movies.domain.model.Rating
import cinescout.movies.domain.testdata.MovieTestData.Inception
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import kotlin.test.*

internal class RemoteMovieDataSourceTest {

    private val tmdbSource: TmdbRemoteMovieDataSource = mockk(relaxUnitFun = true)
    private val traktSource: TraktRemoteMovieDataSource = mockk(relaxUnitFun = true)
    private val remoteMovieDataSource = RemoteMovieDataSource(tmdbSource = tmdbSource, traktSource = traktSource)

    @Test
    fun `post rating posts to tmdb and trakt`() = runTest {
        // given
        val movie = Inception

        // when
        Rating.of(8).tap { rating ->
            remoteMovieDataSource.postRating(movie, rating)

            // then
            coVerify { tmdbSource.postRating(movie, rating) }
            coVerify { traktSource.postRating(movie, rating) }
        }
    }

    @Test
    fun `post watchlist posts to tmdb and trakt`() = runTest {
        // given
        val movie = Inception

        // when
        remoteMovieDataSource.postWatchlist(movie)

        // then
        coVerify { tmdbSource.postWatchlist(movie) }
        coVerify { traktSource.postWatchlist(movie) }
    }
}
