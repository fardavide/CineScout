package network.tmdb

import io.ktor.client.HttpClient
import io.ktor.client.features.defaultRequest
import io.ktor.client.request.parameter
import io.ktor.http.URLProtocol
import network.baseHttpClient
import network.networkModule
import org.koin.core.qualifier.named
import org.koin.dsl.module

val v3Client = named("Tmdb client v3")
val v4Client = named("Tmdb client v4")

val tmdbNetworkModule = module {

    single(v3Client) {
        get<HttpClient>(baseHttpClient).config {
            defaultRequest {
                url {
                    protocol = URLProtocol.HTTPS
                    host = "api.themoviedb.org/3"
                }
                parameter("api_key", TMDB_V3_API_KEY)
            }
        }
    }

    // TODO auth
    single(v4Client) {
        get<HttpClient>(baseHttpClient).config {
            defaultRequest {
                url {
                    protocol = URLProtocol.HTTPS
                    host = "api.themoviedb.org/4"
                }
            }
        }
    }

} + networkModule
