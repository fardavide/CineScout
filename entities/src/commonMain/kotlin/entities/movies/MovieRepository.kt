package entities.movies

import entities.Actor
import entities.FiveYearRange
import entities.Genre

interface MovieRepository {

    suspend fun discover(
        actors: Collection<Actor> = emptySet(),
        genres: Collection<Genre> = emptySet(),
        years: FiveYearRange? = null
    ): Collection<Movie>

    suspend fun search(query: String): Collection<Movie>
}
