package movies.remote.tmdb.movie

import io.ktor.client.*
import io.ktor.client.features.*
import io.ktor.client.request.*
import movies.remote.tmdb.model.MoviePageResult

internal class MovieSearchService(
    client: HttpClient
) {

    private val client = client.config {
        defaultRequest {
            url.path("search", "movie")
            parameter("append_to_response", "credits")
        }
    }

    suspend fun search(query: String) = client.get<MoviePageResult> {
        parameter("query", query )
    }

}
