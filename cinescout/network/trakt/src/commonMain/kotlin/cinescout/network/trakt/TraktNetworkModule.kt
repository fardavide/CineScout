package cinescout.network.trakt

import cinescout.network.NetworkQualifier
import io.ktor.client.HttpClient
import io.ktor.client.plugins.DefaultRequest.DefaultRequestBuilder
import io.ktor.client.plugins.defaultRequest
import io.ktor.client.request.headers
import io.ktor.http.URLProtocol
import org.koin.core.qualifier.named
import org.koin.dsl.module

val NetworkTraktModule = module {

    fun DefaultRequestBuilder.headers(accessToken: String) = headers {
        append("Content-type", "application/json")
        append("trakt-api-key", TRAKT_CLIENT_ID)
        append("trakt-api-version", "2")
        append("Authorization", "Bearer $accessToken")
    }

    single(TraktNetworkQualifier.Client) {
        get<HttpClient>(NetworkQualifier.BaseHttpClient).config {
            defaultRequest {
                url {
                    protocol = URLProtocol.HTTPS
                    host = "api.trakt.tv" // test: https://api-staging.trakt.tv
                }
                headers(get(TraktNetworkQualifier.AccessToken))
            }
        }
    }

    factory(TraktNetworkQualifier.ClientId) { TRAKT_CLIENT_ID }
    factory(TraktNetworkQualifier.ClientSecret) { TRAKT_CLIENT_SECRET }
}

object TraktNetworkQualifier {

    val Client = named("Trakt client")
    val ClientId = named("Trakt client id")
    val ClientSecret = named("Trakt client secret")
    val AccessToken = named("Trakt access token")
    val RefreshToken = named("Trakt refresh token")
}
