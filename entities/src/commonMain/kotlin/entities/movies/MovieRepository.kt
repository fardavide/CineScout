package entities.movies

import entities.Either
import entities.NetworkError
import entities.ResourceError
import entities.TmdbId

interface MovieRepository {

    suspend fun find(id: TmdbId): Either<ResourceError, Movie>

    suspend fun discover(params: DiscoverParams): Either<NetworkError, List<Movie>>

    suspend fun search(query: String): Either<SearchError, List<Movie>>
}

sealed class SearchError {
    object EmptyQuery : SearchError()
    object ShortQuery: SearchError()
    data class Network(val networkError: NetworkError) : SearchError()
}

data class DiscoverParams(
    val actor: TmdbId,
    val genre: TmdbId,
    val year: Int? = null,
)
