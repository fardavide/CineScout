package cinescout.network.tmdb

import cinescout.network.CineScoutClient
import cinescout.network.tmdb.util.sessionId
import io.ktor.client.HttpClient
import io.ktor.client.HttpClientConfig
import io.ktor.client.engine.HttpClientEngine
import io.ktor.client.engine.HttpClientEngineConfig
import io.ktor.client.plugins.DefaultRequest
import io.ktor.client.plugins.auth.Auth
import io.ktor.client.plugins.auth.providers.BearerTokens
import io.ktor.client.plugins.auth.providers.bearer
import io.ktor.client.plugins.defaultRequest
import io.ktor.client.request.bearerAuth
import io.ktor.client.request.headers
import io.ktor.http.ContentType
import io.ktor.http.URLProtocol
import io.ktor.http.contentType

fun CineScoutTmdbV4Client(authProvider: TmdbAuthProvider): HttpClient = CineScoutClient {
    setup(authProvider)
}

fun CineScoutTmdbV4Client(
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
        bearer {
            suspend fun TmdbAuthProvider?.loadTokens(): BearerTokens {
                return BearerTokens(
                    accessToken = this?.accessToken() ?: TMDB_V4_READ_ACCESS_TOKEN,
                    refreshToken = ""
                )
            }

            loadTokens(authProvider::loadTokens)
            refreshTokens { authProvider.loadTokens() }
        }
    }
    defaultRequest {
        url {
            protocol = URLProtocol.HTTPS
            host = "api.themoviedb.org/4"
            parameters.append("api_key", TMDB_V3_API_KEY)
        }
        headers {
            contentType(ContentType.Application.Json)
        }
    }
}

private fun DefaultRequest.DefaultRequestBuilder.headers(accessToken: String?) = headers {
    contentType(ContentType.Application.Json)
    bearerAuth(accessToken ?: TMDB_V4_READ_ACCESS_TOKEN)
}
