package movies.remote.tmdb

import io.ktor.client.*
import io.ktor.client.features.*
import io.ktor.client.request.*
import io.ktor.http.*
import movies.remote.TmdbRemoteMovieSource
import movies.remote.remoteMoviesModule
import movies.remote.tmdb.movie.MovieDiscoverService
import movies.remote.tmdb.movie.MovieSearchService
import movies.remote.tmdb.movie.MovieService
import network.baseHttpClient
import network.networkModule
import org.koin.core.qualifier.named
import org.koin.dsl.module

private val v3Client = named("Tmdb client v3")
private val v4Client = named("Tmdb client v4")

val tmdbRemoteMoviesModule = module {

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

    factory<TmdbRemoteMovieSource> {
        TmdbRemoteMovieSourceImpl(
            movieDiscoverService = get(),
            movieService = get(),
            movieSearchService = get()
        )
    }

    factory { MovieDiscoverService(client = get(v3Client)) }
    factory { MovieService(client = get(v3Client)) }
    factory { MovieSearchService(client = get(v3Client)) }

} + remoteMoviesModule + networkModule
