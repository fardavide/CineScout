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

data class ServiceMockEngines(
    val account: MockEngine,
    val auth: MockEngine,
    val movie: MockEngine,
    val tvShow: MockEngine
) {

    fun all(): MockEngine = account + auth + movie + tvShow

    companion object {

        fun tmdb() = ServiceMockEngines(
            account = MockTmdbAccountEngine(),
            auth = MockTmdbAuthEngine(),
            movie = MockTmdbMovieEngine(),
            tvShow = MockTmdbTvShowEngine()
        )

        fun trakt() = ServiceMockEngines(
            account = MockTraktAccountEngine(),
            auth = MockTraktAuthEngine(),
            movie = MockTraktMovieEngine(),
            tvShow = MockTraktTvShowEngine()
        )
    }
}

object MockEngineQualifier {

    const val Tmdb = "Tmdb Mock Engine"
    const val Trakt = "Trakt Mock Engine"
}
