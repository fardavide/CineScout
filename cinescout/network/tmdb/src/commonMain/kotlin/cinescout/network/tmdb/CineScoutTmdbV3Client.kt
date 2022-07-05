package cinescout.network.tmdb

import cinescout.network.CineScoutClient
import io.ktor.client.HttpClient
import io.ktor.client.HttpClientConfig
import io.ktor.client.engine.HttpClientEngine
import io.ktor.client.engine.HttpClientEngineConfig
import io.ktor.client.plugins.defaultRequest
import io.ktor.http.URLProtocol

fun CineScoutV3Client(): HttpClient = CineScoutClient {
    setup()
}

fun CineScoutV3Client(engine: HttpClientEngine) = CineScoutClient(engine) {
    setup()
}

private fun <T : HttpClientEngineConfig> HttpClientConfig<T>.setup() {
    defaultRequest {
        url {
            protocol = URLProtocol.HTTPS
            host = "api.themoviedb.org/3"
            parameters.append("api_key", TMDB_V3_API_KEY)
            // TODO parameters.append("session_id", get<String>(TmdbNetworkQualifier.SessionId))
        }
    }
}
