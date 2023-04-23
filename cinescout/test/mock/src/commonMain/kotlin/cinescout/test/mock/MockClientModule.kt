package cinescout.test.mock

import cinescout.network.tmdb.CineScoutTmdbClient
import cinescout.network.trakt.CineScoutTraktClient
import cinescout.network.trakt.CineScoutTraktRefreshTokenClient
import cinescout.network.trakt.RefreshTraktAccessToken
import cinescout.network.trakt.TraktAuthProvider
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
    fun cineScoutTmdbClient(@Named(MockEngineQualifier.Tmdb) engine: MockEngine) = CineScoutTmdbClient(
        engine = engine,
        logBody = true
    )

    @Single
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
    fun cineScoutTraktRefreshTokenClient(@Named(MockEngineQualifier.Trakt) engine: MockEngine) =
        CineScoutTraktRefreshTokenClient(
            engine = engine
        )
}

@Factory internal class Empty
