package cinescout.movies.data.remote.trakt

import cinescout.movies.data.remote.trakt.testutil.MockTraktMovieEngine
import cinescout.movies.domain.model.Rating
import cinescout.movies.domain.testdata.MovieTestData
import cinescout.network.CineScoutClient
import kotlinx.coroutines.test.runTest
import kotlin.test.Test

internal class RealTraktMovieDataSourceTest {

    private val client = CineScoutClient(MockTraktMovieEngine())
    private val dataSource = RealTraktMovieDataSource(client)

    @Test
    fun `post watchlist does nothing`() = runTest {
        // given
        val movie = MovieTestData.Inception

        // when
        dataSource.postWatchlist(movie)

        // then TODO
    }

    @Test
    fun `post rating does nothing`() = runTest {
        // given
        val movie = MovieTestData.Inception
        Rating.of(8).tap { rating ->

            // when
            dataSource.postRating(movie, rating)

            // then TODO
        }
    }
}
