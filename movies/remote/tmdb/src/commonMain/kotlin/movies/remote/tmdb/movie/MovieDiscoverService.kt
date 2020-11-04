package movies.remote.tmdb.movie

import entities.field.Actor
import entities.field.FiveYearRange
import entities.field.Genre
import entities.movies.DiscoverParams
import io.ktor.client.HttpClient
import io.ktor.client.features.defaultRequest
import io.ktor.client.request.get
import io.ktor.client.request.parameter
import movies.remote.tmdb.model.MoviePageResult
import util.useIfNotEmpty
import kotlin.random.Random

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
            parameter("with_people", actors.random().id.i )
        }
        genres.useIfNotEmpty {
            parameter("with_genres", genres.random().id.i )
        }
        if (Random.nextBoolean()) {
            years?.let {
                parameter("year", it.range.random())
            }
        }
    }

    suspend fun discover(params: DiscoverParams) = client.get<MoviePageResult> {
        parameter("with_people", params.actor.i)
        parameter("with_genres", params.genre.i)
        params.year?.let { parameter("year", it) }
    }

}
