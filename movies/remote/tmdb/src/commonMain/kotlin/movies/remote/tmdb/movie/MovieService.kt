package movies.remote.tmdb.movie

import entities.Either
import entities.NetworkError
import io.ktor.client.HttpClient
import io.ktor.client.features.defaultRequest
import io.ktor.client.request.get
import io.ktor.client.request.parameter
import movies.remote.tmdb.model.MovieDetails
import network.Try

class MovieService(
    client: HttpClient
) {

    private val client = client.config {
        defaultRequest {
            parameter("append_to_response", "credits,images,videos")
        }
    }

    suspend fun details(id: Int): Either<NetworkError, MovieDetails> = Either.Try {
        client.get {
            url.path("movie", "$id")
        }
    }

}
