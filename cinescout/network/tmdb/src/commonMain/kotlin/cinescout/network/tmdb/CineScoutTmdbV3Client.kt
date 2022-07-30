package cinescout.network.tmdb

import cinescout.network.CineScoutClient
import io.ktor.client.HttpClient
import io.ktor.client.HttpClientConfig
import io.ktor.client.engine.HttpClientEngine
import io.ktor.client.engine.HttpClientEngineConfig
import io.ktor.client.plugins.defaultRequest
import io.ktor.http.URLProtocol

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
    defaultRequest {
        url {
            protocol = URLProtocol.HTTPS
            host = "api.themoviedb.org/3"
            parameters.append("api_key", TMDB_V3_API_KEY)
            authProvider?.sessionId()?.let { sessionId ->
                parameters.append(TmdbParameters.SessionId, sessionId)
            }
        }
    }
}
