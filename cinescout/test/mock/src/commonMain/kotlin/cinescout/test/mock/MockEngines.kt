package cinescout.test.mock

import cinescout.account.tmdb.data.remote.testutil.MockTmdbAccountEngine
import cinescout.account.trakt.data.remote.testutil.MockTraktAccountEngine
import cinescout.auth.tmdb.data.remote.testutil.MockTmdbAuthEngine
import cinescout.auth.trakt.data.remote.testutil.MockTraktAuthEngine
import cinescout.movies.data.remote.tmdb.testutil.MockTmdbMovieEngine
import cinescout.movies.data.remote.trakt.testutil.MockTraktMovieEngine
import cinescout.network.testutil.plus
import cinescout.tvshows.data.remote.tmdb.testutil.MockTmdbTvShowEngine
import cinescout.tvshows.data.remote.trakt.testutil.MockTraktTvShowEngine
import io.ktor.client.engine.mock.MockEngine

object MockEngines {

    val tmdb = ServiceMockEngines(
        account = MockTmdbAccountEngine(),
        auth = MockTmdbAuthEngine(),
        movie = MockTmdbMovieEngine(),
        tvShow = MockTmdbTvShowEngine()
    )

    val trakt = ServiceMockEngines(
        account = MockTraktAccountEngine(),
        auth = MockTraktAuthEngine(),
        movie = MockTraktMovieEngine(),
        tvShow = MockTraktTvShowEngine()
    )

    fun tmdb() = tmdb.account + tmdb.auth + tmdb.movie + tmdb.tvShow
    fun trakt() = trakt.account + trakt.auth + trakt.movie + trakt.tvShow
}

data class ServiceMockEngines(
    val account: MockEngine,
    val auth: MockEngine,
    val movie: MockEngine,
    val tvShow: MockEngine
)
