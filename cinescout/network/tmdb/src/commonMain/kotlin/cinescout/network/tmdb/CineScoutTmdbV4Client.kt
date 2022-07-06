package cinescout.network.tmdb

import cinescout.network.CineScoutClient
import io.ktor.client.HttpClient
import io.ktor.client.HttpClientConfig
import io.ktor.client.engine.HttpClientEngine
import io.ktor.client.engine.HttpClientEngineConfig
import io.ktor.client.plugins.DefaultRequest
import io.ktor.client.plugins.defaultRequest
import io.ktor.client.request.bearerAuth
import io.ktor.client.request.headers
import io.ktor.http.ContentType
import io.ktor.http.URLProtocol
import io.ktor.http.contentType

fun CineScoutTmdbV4Client(): HttpClient = CineScoutClient {
    setup()
}

fun CineScoutTmdbV4Client(engine: HttpClientEngine) = CineScoutClient(engine) {
    setup()
}

private fun <T : HttpClientEngineConfig> HttpClientConfig<T>.setup() {
    defaultRequest {
        url {
            protocol = URLProtocol.HTTPS
            host = "api.themoviedb.org/4"
            parameters.append("api_key", TMDB_V3_API_KEY)
            // TODO parameters.append("session_id", get<String>(TmdbNetworkQualifier.SessionId))
        }
        headers(accessToken = null)
    }
}

private fun DefaultRequest.DefaultRequestBuilder.headers(accessToken: String?) = headers {
    contentType(ContentType.Application.Json)
    bearerAuth(accessToken ?: TMDB_V4_READ_ACCESS_TOKEN)
}
