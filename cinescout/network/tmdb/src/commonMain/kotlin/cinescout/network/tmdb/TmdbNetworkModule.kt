package cinescout.network.tmdb

import cinescout.network.NetworkQualifier
import io.ktor.client.HttpClient
import io.ktor.client.plugins.DefaultRequest.DefaultRequestBuilder
import io.ktor.client.plugins.defaultRequest
import io.ktor.client.request.headers
import io.ktor.http.URLProtocol
import io.ktor.http.parametersOf
import org.koin.core.qualifier.named
import org.koin.dsl.module
import shuttle.utils.kotlin.takeIfNotBlank

val TmdbNetworkModule = module {

    fun DefaultRequestBuilder.headers(accessToken: String) = headers {
        append("Content-Type", "application/json;charset=utf-8")
        append("Authorization", "Bearer ${accessToken.takeIfNotBlank() ?: TMDB_V4_READ_ACCESS_TOKEN}")
    }

    @Suppress("DEPRECATION")
    single(TmdbNetworkQualifier.V3Client) {
        get<HttpClient>(TmdbNetworkQualifier.V4Client).config {
            defaultRequest {
                url {
                    protocol = URLProtocol.HTTPS
                    host = "api.themoviedb.org/3"
                }
                parametersOf("api_key", TMDB_V3_API_KEY)
                parametersOf("session_id", get<String>(TmdbNetworkQualifier.SessionId))
            }
        }
    }

    single(TmdbNetworkQualifier.V4Client) {
        get<HttpClient>(NetworkQualifier.BaseHttpClient).config {
            defaultRequest {
                url {
                    protocol = URLProtocol.HTTPS
                    host = "api.themoviedb.org"
                }
                headers(get(TmdbNetworkQualifier.V4accessToken))
                parametersOf("api_key", TMDB_V3_API_KEY)
                parametersOf("session_id", get<String>(TmdbNetworkQualifier.SessionId))
            }
        }
    }
}

object TmdbNetworkQualifier {

    @Deprecated("Use v4 client")
    val V3Client = named("Tmdb client v3")
    val V4Client = named("Tmdb client v4")
    val V4accessToken = named("Tmdb v4 access token")
    val V3accountId = named("Tmdb V3 current account Id")
    val V4accountId = named("Tmdb V4 current account Id")
    val SessionId = named("Tmdb current session Id")
}
