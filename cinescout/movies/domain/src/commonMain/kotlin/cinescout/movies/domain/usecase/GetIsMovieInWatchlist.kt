package cinescout.movies.domain.usecase

import arrow.core.Either
import cinescout.error.NetworkError
import cinescout.movies.domain.model.TmdbMovieId
import cinescout.store5.ext.filterData
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import org.koin.core.annotation.Factory

@Factory
class GetIsMovieInWatchlist(
    private val getAllWatchlistMovies: GetAllWatchlistMovies
) {

    operator fun invoke(id: TmdbMovieId, refresh: Boolean = true): Flow<Either<NetworkError, Boolean>> =
        getAllWatchlistMovies(refresh).filterData().map { either ->
            either.map { movies ->
                movies.any { it.tmdbId == id }
            }
        }
}
