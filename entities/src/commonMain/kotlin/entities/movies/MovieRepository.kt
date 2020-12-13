package entities.movies

import entities.Either
import entities.NetworkError
import entities.ResourceError
import entities.TmdbId

interface MovieRepository {

    suspend fun find(id: TmdbId): Either<ResourceError, Movie>

    suspend fun discover(params: DiscoverParams): Collection<Movie>

    suspend fun search(query: String): Collection<Movie>
}

data class DiscoverParams(
    val actor: TmdbId,
    val genre: TmdbId,
    val year: Int? = null,
)
