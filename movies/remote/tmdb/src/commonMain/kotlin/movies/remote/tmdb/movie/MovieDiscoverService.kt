package movies.remote.tmdb.movie

import Actor
import FiveYearRange
import Genre
import io.ktor.client.*
import io.ktor.client.features.*
import io.ktor.client.request.*
import movies.remote.tmdb.model.MoviePageResult
import util.useIfNotEmpty

internal class MovieDiscoverService(
    client: HttpClient
) {

    private val client = client.config {
        defaultRequest {
            url.path("discover", "movie")
            parameter("append_to_response", "credits")
        }
    }

    suspend fun discover(
        actors: Collection<Actor>,
        genres: Collection<Genre>,
        years: FiveYearRange?,
    ) = client.get<MoviePageResult> {
        actors.useIfNotEmpty {
            parameter("with_cast", actors.random().id.i )
        }
        genres.useIfNotEmpty {
            parameter("with_genres", genres.random().id.i )
        }
        years?.let {
            parameter("year", it.range.random())
        }
    }

}
