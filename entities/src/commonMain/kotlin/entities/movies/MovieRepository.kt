package entities.movies

import entities.Actor
import entities.FiveYearRange
import entities.Genre
import entities.TmdbId

interface MovieRepository {

    suspend fun find(id: TmdbId): Movie?

    suspend fun discover(
        actors: Collection<Actor> = emptySet(),
        genres: Collection<Genre> = emptySet(),
        years: FiveYearRange? = null
    ): Collection<Movie>

    suspend fun search(query: String): Collection<Movie>
}
