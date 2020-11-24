package network.trakt

import io.ktor.client.HttpClient
import io.ktor.client.features.defaultRequest
import io.ktor.client.request.HttpRequestBuilder
import io.ktor.client.request.headers
import io.ktor.http.URLProtocol
import network.baseHttpClient
import network.networkModule
import org.koin.core.qualifier.named
import org.koin.dsl.module

val client = named("Trakt client")
val clientId = named("Trakt client id")
val clientSecret = named("Trakt client secret")
val accessToken = named("Trakt access token")
val refreshToken = named("Trakt refresh token")

val traktNetworkModule = module {

    fun HttpRequestBuilder.headers(accessToken: String) = headers {
        append("Content-type", "application/json")
        append("trakt-api-key", TRAKT_CLIENT_ID)
        append("trakt-api-version", "2")
        append("Authorization", "Bearer $accessToken")
    }

    single(client) {
        get<HttpClient>(baseHttpClient).config {
            defaultRequest {
                url {
                    protocol = URLProtocol.HTTPS
                    host = "api.trakt.tv" // test: https://api-staging.trakt.tv
                }
                headers(get(accessToken))
            }
        }
    }

    factory(clientId) { TRAKT_CLIENT_ID }
    factory(clientSecret) { TRAKT_CLIENT_SECRET }

} + networkModule
