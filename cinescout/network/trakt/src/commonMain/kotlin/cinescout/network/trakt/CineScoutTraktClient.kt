package cinescout.network.trakt

import cinescout.network.CineScoutClient
import io.ktor.client.HttpClientConfig
import io.ktor.client.engine.HttpClientEngine
import io.ktor.client.engine.HttpClientEngineConfig
import io.ktor.client.plugins.auth.Auth
import io.ktor.client.plugins.auth.providers.BearerTokens
import io.ktor.client.plugins.auth.providers.bearer
import io.ktor.client.plugins.defaultRequest
import io.ktor.client.request.headers
import io.ktor.http.ContentType
import io.ktor.http.URLProtocol
import io.ktor.http.contentType

fun CineScoutTraktClient(authProvider: TraktAuthProvider) = CineScoutClient {
    setup(authProvider)
}

fun CineScoutTraktClient(engine: HttpClientEngine, authProvider: TraktAuthProvider? = null) =
    CineScoutClient(engine) {
        setup(authProvider)
    }

private fun <T : HttpClientEngineConfig> HttpClientConfig<T>.setup(authProvider: TraktAuthProvider?) {
    install(Auth) {
        bearer {
            suspend fun TraktAuthProvider?.loadTokens(): BearerTokens? {
                this
                    ?: return null
                val accessToken = accessToken()
                    ?: return null
                val refreshToken = refreshToken()
                    ?: return null

                return BearerTokens(accessToken = accessToken, refreshToken = refreshToken)
            }

            loadTokens(authProvider::loadTokens)
            refreshTokens { authProvider.loadTokens() }
        }
    }
    defaultRequest {
        url {
            protocol = URLProtocol.HTTPS
            host = "api.trakt.tv" // test: https://api-staging.trakt.tv
        }
        headers {
            contentType(ContentType.Application.Json)
            append("trakt-api-key", TRAKT_CLIENT_ID)
            append("trakt-api-version", "2")
        }
    }
}
