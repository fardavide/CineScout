package cinescout.network.tmdb

import cinescout.network.CineScoutClient
import cinescout.network.tmdb.util.sessionId
import io.ktor.client.HttpClient
import io.ktor.client.HttpClientConfig
import io.ktor.client.engine.HttpClientEngine
import io.ktor.client.engine.HttpClientEngineConfig
import io.ktor.client.plugins.auth.Auth
import io.ktor.client.plugins.defaultRequest
import io.ktor.http.URLProtocol
import org.koin.core.annotation.Named
import org.koin.core.annotation.Single

@Single
@Named(TmdbNetworkQualifier.V3.Client)
fun CineScoutTmdbV3Client(authProvider: TmdbAuthProvider): HttpClient = CineScoutClient {
    setup(authProvider)
}

fun CineScoutTmdbV3Client(
    engine: HttpClientEngine,
    authProvider: TmdbAuthProvider? = null
) = CineScoutClient(engine) {
    setup(authProvider)
}

private fun <T : HttpClientEngineConfig> HttpClientConfig<T>.setup(authProvider: TmdbAuthProvider?) {
    install(Auth) {
        sessionId {
            sessionId { authProvider?.sessionId() }
        }
    }
    defaultRequest {
        url {
            protocol = URLProtocol.HTTPS
            host = "api.themoviedb.org/3"
            parameters.append("api_key", TMDB_V3_API_KEY)
        }
    }
}
