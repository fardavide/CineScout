package movies.remote.tmdb.movie

import io.ktor.client.HttpClient
import io.ktor.client.features.defaultRequest
import io.ktor.client.request.get
import io.ktor.client.request.parameter
import movies.remote.tmdb.model.MovieDetails

class MovieService(
    client: HttpClient
) {

    private val client = client.config {
        defaultRequest {
            parameter("append_to_response", "credits,images,videos")
        }
    }

    suspend fun details(id: Int) = client.get<MovieDetails> {
        url.path("movie", "$id")
    }

}
