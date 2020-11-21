package domain.stats

import entities.Either
import entities.ResourceError
import entities.left
import entities.movies.Movie
import entities.right
import entities.stats.StatRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class GetMoviesInWatchlist(
    private val stats: StatRepository
) {

    operator fun invoke(): Flow<Either<Error, State>> =
        stats.watchlist().map { either ->
            when (either) {
                is Either.Right -> {
                    val list = either.b
                    if (list.isNotEmpty()) State.Success(list).right()
                    else Error.NoMovies.left()
                }
                is Either.Left -> Error.Unknown(either.a).left()
            }
        }

    sealed class State {
        object Loading : State()
        data class Success(val movies: Collection<Movie>) : State()
    }

    sealed class Error : entities.Error {
        object NoMovies : GetMoviesInWatchlist.Error()
        data class Unknown(val resourceError: ResourceError) : GetMoviesInWatchlist.Error()
    }
}
