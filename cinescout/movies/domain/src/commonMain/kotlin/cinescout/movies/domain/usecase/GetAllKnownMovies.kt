package cinescout.movies.domain.usecase

import arrow.core.Either
import cinescout.error.DataError
import cinescout.movies.domain.model.Movie
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

/**
 * A known movie is any movie that the user rated, added to watchlist, watched or similar
 */
class GetAllKnownMovies(
    private val getAllRatedMovies: GetAllRatedMovies
) {

    operator fun invoke(): Flow<Either<DataError, List<Movie>>> =
        getAllRatedMovies().map { listEither ->
            listEither.map { list ->
                list.data.map { movieWithRating -> movieWithRating.movie }
            }
        }
}
