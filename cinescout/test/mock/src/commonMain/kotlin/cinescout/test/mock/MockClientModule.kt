package cinescout.test.mock

import cinescout.network.tmdb.CineScoutTmdbClient
import cinescout.network.tmdb.TmdbNetworkQualifier
import cinescout.network.trakt.CineScoutTraktClient
import cinescout.network.trakt.CineScoutTraktRefreshTokenClient
import cinescout.network.trakt.TraktNetworkQualifier
import io.ktor.client.engine.mock.MockEngine
import org.koin.core.qualifier.named
import org.koin.dsl.module

val MockClientModule = module {
    factory(named(TmdbNetworkQualifier.Client)) {
        CineScoutTmdbClient(
            engine = get<MockEngine>(named(MockEngineQualifier.Tmdb))
        )
    }
    factory(named(TraktNetworkQualifier.Client)) {
        CineScoutTraktClient(
            engine = get<MockEngine>(named(MockEngineQualifier.Trakt)),
            authProvider = get(),
            refreshAccessToken = get()
        )
    }
    factory(named(TraktNetworkQualifier.RefreshTokenClient)) {
        CineScoutTraktRefreshTokenClient(
            engine = get<MockEngine>(named(MockEngineQualifier.Trakt))
        )
    }
}
