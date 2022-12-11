package cinescout.test.mock

import cinescout.network.tmdb.CineScoutTmdbV3Client
import cinescout.network.tmdb.CineScoutTmdbV4Client
import cinescout.network.tmdb.TmdbNetworkQualifier
import cinescout.network.trakt.CineScoutTraktClient
import cinescout.network.trakt.TraktNetworkQualifier
import org.koin.core.qualifier.named
import org.koin.dsl.module

val MockClientModule = module {
    factory(named(TmdbNetworkQualifier.V3.Client)) {
        CineScoutTmdbV3Client(engine = MockEngines.tmdb(), authProvider = get())
    }
    factory(named(TmdbNetworkQualifier.V4.Client)) {
        CineScoutTmdbV4Client(engine = MockEngines.tmdb(), authProvider = get())
    }
    factory(named(TraktNetworkQualifier.Client)) {
        CineScoutTraktClient(
            engine = MockEngines.trakt(),
            authProvider = get()
        )
    }
}
