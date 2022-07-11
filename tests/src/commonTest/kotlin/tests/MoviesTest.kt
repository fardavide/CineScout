package tests

import arrow.core.right
import cinescout.movies.data.remote.tmdb.testutil.MockTmdbMovieEngine
import cinescout.movies.domain.model.Rating
import cinescout.movies.domain.testdata.MovieTestData
import cinescout.movies.domain.testdata.TmdbMovieIdTestData
import cinescout.movies.domain.usecase.GetMovie
import cinescout.movies.domain.usecase.RateMovie
import cinescout.network.CineScoutClient
import cinescout.network.tmdb.TmdbNetworkQualifier
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import org.koin.dsl.module
import org.koin.test.inject
import util.BaseAppTest
import kotlin.test.Test
import kotlin.test.assertEquals

class MoviesTest : BaseAppTest() {

    private val getMovie: GetMovie by inject()
    private val rateMovie: RateMovie by inject()

    override val extraModule = module {
        factory(TmdbNetworkQualifier.V3.Client) { CineScoutClient(MockTmdbMovieEngine()) }
    }

    @Test
    fun `get movie`() = runTest {
        // given
        val movie = MovieTestData.TheWolfOfWallStreet

        // when
        val result = getMovie(TmdbMovieIdTestData.TheWolfOfWallStreet).first()

        // then
        assertEquals(movie.right(), result)
    }

    @Test
    fun `rate movie`() = runTest {
        // given
        val movie = MovieTestData.TheWolfOfWallStreet
        Rating.of(8).tap { rating ->

            // when
            val result = rateMovie(movie, rating)

            // then
            assertEquals(Unit.right(), result)
        }
    }
}
