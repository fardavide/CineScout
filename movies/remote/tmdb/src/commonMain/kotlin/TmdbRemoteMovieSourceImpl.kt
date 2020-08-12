import io.ktor.client.*
import io.ktor.client.features.*
import io.ktor.client.request.*
import movies.Movie
import util.useIfNotEmpty

internal class TmdbRemoteMovieSourceImpl(
    client: HttpClient
) : TmdbRemoteMovieSource {

    private val client = client.config {
        defaultRequest {
            url.path("3", "discover", "movie")
            parameter("append_to_response", "credits")
        }
    }

    override suspend fun discover(
        actors: Collection<Actor>,
        genres: Collection<Name>,
        years: FiveYearRange?,
    ): Collection<Movie> = client.get {
        actors.useIfNotEmpty {
            parameter("with_people", actors.joinToString { it.id.s })
        }

    }

    override suspend fun search(query: String): Collection<Movie> {
        TODO("Not yet implemented")
    }
}
