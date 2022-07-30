package cinescout.movies.domain.usecase

import cinescout.movies.domain.model.Movie
import cinescout.store.PagedStore
import cinescout.store.map

/**
 * A known movie is any movie that the user rated, added to watchlist, watched or similar
 */
class GetAllKnownMovies(
    private val getAllRatedMovies: GetAllRatedMovies
) {

    operator fun invoke(): PagedStore<Movie> =
        getAllRatedMovies().map { listEither ->
            listEither.map { list ->
                list.map { movieWithRating -> movieWithRating.movie }
            }
        }
}
