package cinescout.movies.domain.usecase

import arrow.core.Either
import cinescout.error.DataError
import cinescout.movies.domain.model.TmdbMovieId
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import org.koin.core.annotation.Factory
import store.Refresh
import kotlin.time.Duration.Companion.minutes

@Factory
class GetIsMovieInWatchlist(
    private val getAllWatchlistMovies: GetAllWatchlistMovies
) {

    operator fun invoke(
        id: TmdbMovieId,
        refresh: Refresh = Refresh.IfExpired(validity = 5.minutes)
    ): Flow<Either<DataError, Boolean>> =
        getAllWatchlistMovies(refresh).map { either ->
            either.map { movies ->
                movies.data.any { it.tmdbId == id }
            }
        }
}
