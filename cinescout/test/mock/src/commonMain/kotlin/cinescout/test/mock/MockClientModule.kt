package cinescout.test.mock

import cinescout.network.tmdb.CineScoutTmdbClient
import cinescout.network.tmdb.TmdbNetworkQualifier
import cinescout.network.trakt.CineScoutTraktClient
import cinescout.network.trakt.CineScoutTraktRefreshTokenClient
import cinescout.network.trakt.RefreshTraktAccessToken
import cinescout.network.trakt.TraktAuthProvider
import cinescout.network.trakt.TraktClient
import cinescout.network.trakt.TraktRefreshTokenClient
import io.ktor.client.engine.mock.MockEngine
import org.koin.core.annotation.ComponentScan
import org.koin.core.annotation.Factory
import org.koin.core.annotation.Module
import org.koin.core.annotation.Named
import org.koin.core.annotation.Single

@Module
@ComponentScan
class MockClientModule {

    @Single
    @Named(TmdbNetworkQualifier.Client)
    fun cineScoutTmdbClient(@Named(MockEngineQualifier.Tmdb) engine: MockEngine) = CineScoutTmdbClient(
        engine = engine,
        logBody = true
    )

    @Single
    @Named(TraktClient)
    fun cineScoutTraktClient(
        authProvider: TraktAuthProvider,
        @Named(MockEngineQualifier.Trakt) engine: MockEngine,
        refreshAccessToken: RefreshTraktAccessToken
    ) = CineScoutTraktClient(
        engine = engine,
        authProvider = authProvider,
        refreshAccessToken = refreshAccessToken,
        logBody = true
    )

    @Single
    @Named(TraktRefreshTokenClient)
    fun cineScoutTraktRefreshTokenClient(@Named(MockEngineQualifier.Trakt) engine: MockEngine) =
        CineScoutTraktRefreshTokenClient(
            engine = engine
        )
}

@Factory internal class Empty
