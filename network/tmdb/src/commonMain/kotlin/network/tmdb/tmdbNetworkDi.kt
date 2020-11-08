package network.tmdb

import io.ktor.client.HttpClient
import io.ktor.client.features.defaultRequest
import io.ktor.client.request.HttpRequestBuilder
import io.ktor.client.request.headers
import io.ktor.client.request.parameter
import io.ktor.http.URLProtocol
import network.baseHttpClient
import network.networkModule
import org.koin.core.qualifier.named
import org.koin.dsl.module
import util.takeIfNotBlank

val v3Client = named("Tmdb client v3")
val v4Client = named("Tmdb client v4")
val v4accessToken = named("Tmdb v4 access token")
val accountId = named("Tmdb current account Id")

val tmdbNetworkModule = module {

    fun HttpRequestBuilder.headers(accessToken: String) = headers {
        append("Content-Type", "application/json;charset=utf-8")
        append("Authorization", "Bearer ${accessToken.takeIfNotBlank() ?: TMDB_V4_READ_ACCESS_TOKEN}")
    }

    single(v3Client) {
        get<HttpClient>(baseHttpClient).config {
            defaultRequest {
                headers(get(v4accessToken))
                url {
                    protocol = URLProtocol.HTTPS
                    host = "api.themoviedb.org/3"
                }
                parameter("api_key", TMDB_V3_API_KEY)
            }
        }
    }

    single(v4Client) {
        get<HttpClient>(baseHttpClient).config {
            defaultRequest {
                headers(get(v4accessToken))
                url {
                    protocol = URLProtocol.HTTPS
                    host = "api.themoviedb.org/4"
                }
                parameter("api_key", TMDB_V3_API_KEY)
            }
        }
    }

} + networkModule
