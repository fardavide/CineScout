package cinescout.test.mock

import cinescout.account.tmdb.data.remote.testutil.MockTmdbAccountEngine
import cinescout.account.trakt.data.remote.testutil.MockTraktAccountEngine
import cinescout.auth.tmdb.data.remote.testutil.MockTmdbAuthEngine
import cinescout.auth.trakt.data.remote.testutil.MockTraktAuthEngine
import cinescout.movies.data.remote.tmdb.testutil.MockTmdbMovieEngine
import cinescout.movies.data.remote.trakt.testutil.MockTraktMovieEngine
import cinescout.network.testutil.plus
import cinescout.network.tmdb.CineScoutTmdbV3Client
import cinescout.network.tmdb.CineScoutTmdbV4Client
import cinescout.network.tmdb.TmdbNetworkQualifier
import cinescout.network.trakt.CineScoutTraktClient
import cinescout.network.trakt.TraktNetworkQualifier
import org.koin.dsl.module

// TODO Mock Tv Show Engine
val MockClientModule = module {
    factory(TmdbNetworkQualifier.V3.Client) {
        CineScoutTmdbV3Client(
            engine = MockTmdbAccountEngine() + MockTmdbAuthEngine() + MockTmdbMovieEngine(),
            authProvider = get()
        )
    }
    factory(TmdbNetworkQualifier.V4.Client) {
        CineScoutTmdbV4Client(
            engine = MockTmdbAuthEngine(),
            authProvider = get()
        )
    }
    factory(TraktNetworkQualifier.Client) {
        CineScoutTraktClient(
            engine = MockTraktAccountEngine() + MockTraktAuthEngine() + MockTraktMovieEngine(),
            authProvider = get()
        )
    }
}
