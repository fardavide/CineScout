package cinescout.tvshows.domain.usecase

import arrow.core.Either
import arrow.core.Option
import arrow.core.none
import arrow.core.some
import cinescout.error.DataError
import cinescout.screenplay.domain.model.Rating
import cinescout.tvshows.domain.model.TmdbTvShowId
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import org.koin.core.annotation.Factory
import store.Refresh
import kotlin.time.Duration.Companion.minutes

@Factory
class GetTvShowPersonalRating(
    private val getAllRatedTvShows: GetAllRatedTvShows
) {

    operator fun invoke(
        tvShowId: TmdbTvShowId,
        refresh: Refresh = Refresh.IfExpired(validity = 5.minutes)
    ): Flow<Either<DataError, Option<Rating>>> = getAllRatedTvShows(refresh).map { either ->
        either.map { tvShows ->
            tvShows.find { it.tvShow.tmdbId == tvShowId }?.personalRating?.some() ?: none()
        }
    }
}
