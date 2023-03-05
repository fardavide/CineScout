package cinescout.test.mock

import cinescout.account.trakt.data.remote.testutil.MockTraktAccountEngine
import cinescout.auth.trakt.data.remote.testutil.MockTraktAuthEngine
import cinescout.movies.data.remote.tmdb.testutil.MockTmdbMovieEngine
import cinescout.movies.data.remote.trakt.testutil.MockTraktMovieEngine
import cinescout.network.testutil.plus
import cinescout.tvshows.data.remote.tmdb.testutil.MockTmdbTvShowEngine
import cinescout.tvshows.data.remote.trakt.testutil.MockTraktTvShowEngine

object MockEngines {
    
    fun tmdb() = MockTmdbMovieEngine() + MockTmdbTvShowEngine()
    fun trakt() =
        MockTraktAuthEngine() + MockTraktAccountEngine() + MockTraktMovieEngine() + MockTraktTvShowEngine()
}

object MockEngineQualifier {

    const val Tmdb = "Tmdb Mock Engine"
    const val Trakt = "Trakt Mock Engine"
}
