import io.ktor.client.*
import io.ktor.client.features.*
import io.ktor.client.request.*
import io.ktor.http.*
import org.koin.core.qualifier.named
import org.koin.dsl.module

val baseHttpClientQualifier = named("Base Http client")

private val v3Client = named("Tmdb client v3")
private val v4Client = named("Tmdb client v4")

val tmdbRemoteMoviesModule = module {

    single(v3Client) {
        get<HttpClient>(baseHttpClientQualifier).config {
            defaultRequest {
                url {
                    protocol = URLProtocol.HTTPS
                    host = "api.themoviedb.org/3"
                }
                parameter("api_key", TMDB_V3_API_KEY)
            }
        }
    }

    factory<TmdbRemoteMovieSource> { TmdbRemoteMovieSourceImpl(get(v3Client)) }

} + remoteMoviesModule
