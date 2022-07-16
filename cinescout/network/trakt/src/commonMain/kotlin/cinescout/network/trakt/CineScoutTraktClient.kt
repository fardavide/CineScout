package cinescout.network.trakt

import cinescout.network.CineScoutClient
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

fun CineScoutTraktClient() = CineScoutClient {
    setup()
}

fun CineScoutTraktClient(engine: HttpClientEngine) = CineScoutClient(engine) {
    setup()
}

private fun <T : HttpClientEngineConfig> HttpClientConfig<T>.setup() {
    defaultRequest {
        url {
            protocol = URLProtocol.HTTPS
            host = "api.trakt.tv" // test: https://api-staging.trakt.tv
        }
        // TODO: headers(accessToken)
    }
}

private fun DefaultRequest.DefaultRequestBuilder.headers(accessToken: String?) = headers {
    contentType(ContentType.Application.Json)
    append("trakt-api-key", TRAKT_CLIENT_ID)
    append("trakt-api-version", "2")
    if (accessToken != null) {
        bearerAuth(accessToken)
    }
}
