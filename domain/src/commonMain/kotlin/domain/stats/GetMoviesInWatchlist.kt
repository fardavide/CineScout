package domain.stats

import entities.Either
import entities.ResourceError
import entities.left
import entities.movies.Movie
import entities.right
import entities.stats.StatRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import util.unsupported

class GetMoviesInWatchlist(
    private val stats: StatRepository
) {

    operator fun invoke(sorting: MoviesSorting<*> = MoviesSorting.None): Flow<Either<Error, State>> =
        stats.watchlist().map { either ->
            when (either) {
                is Either.Right -> {
                    val list = either.b
                    if (list.isNotEmpty()) State.Success(sorting.sort(list)).right()
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

sealed class MoviesSorting<R : Comparable<R>>(private val selector: (Movie) -> R) {

    abstract val direction: Direction
    enum class Direction {
        Ascendant,
        Descendant
    }

    fun sort(movies: Collection<Movie>): Collection<Movie> {
        if (this is None) return movies
        return when(direction) {
            Direction.Ascendant -> movies.sortedBy(selector)
            Direction.Descendant -> movies.sortedByDescending(selector)
        }
    }

    object None : MoviesSorting<Nothing>({ unsupported }) {

        override val direction = Direction.Ascendant
    }

    data class Alphabetical(override val direction: Direction):
        MoviesSorting<String>({ it.name.s })

    data class Popularity(override val direction: Direction):
        MoviesSorting<Nothing>({ TODO("Sort by popularity") })

    data class Rating(override val direction: Direction):
        MoviesSorting<Double>({ it.rating.average })

    data class ReleaseDate(override val direction: Direction):
        MoviesSorting<UInt>({ it.year })
}
