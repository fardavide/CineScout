package movies.remote.tmdb.movie

import io.ktor.client.*
import io.ktor.client.features.*
import io.ktor.client.request.*
import movies.remote.tmdb.model.MovieDetails

internal class MovieService(
    client: HttpClient
) {

    private val client = client.config {
        defaultRequest {
            parameter("append_to_response", "credits, videos")
        }
    }

    suspend fun details(id: Int) = client.get<MovieDetails> {
        url.path("movie", "$id")
    }

}
