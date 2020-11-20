package domain.stats

import entities.Either
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
        stats.watchlist().map { list ->
            if (list.isNotEmpty()) State.Success(list).right()
            else Error.NoMovies.left()
        }

    sealed class State {
        object Loading : State()
        data class Success(val movies: Collection<Movie>) : State()
    }

    sealed class Error : entities.Error {
        object NoMovies : GetMoviesInWatchlist.Error()
        data class Unknown(val throwable: Throwable) : GetMoviesInWatchlist.Error()
    }
}
