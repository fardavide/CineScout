package entities.movies

import entities.TmdbId

interface MovieRepository {

    suspend fun find(id: TmdbId): Movie?

    suspend fun discover(params: DiscoverParams): Collection<Movie>

    suspend fun search(query: String): Collection<Movie>
}

data class DiscoverParams(
    val actor: TmdbId,
    val genre: TmdbId,
    val year: Int? = null,
)
