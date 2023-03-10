package cinescout.tvshows.domain.usecase

import arrow.core.Either
import arrow.core.Option
import arrow.core.none
import arrow.core.some
import cinescout.error.NetworkError
import cinescout.screenplay.domain.model.Rating
import cinescout.store5.ext.filterData
import cinescout.tvshows.domain.model.TmdbTvShowId
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import org.koin.core.annotation.Factory

@Factory
class GetTvShowPersonalRating(
    private val getAllRatedTvShows: GetAllRatedTvShows
) {

    operator fun invoke(
        tvShowId: TmdbTvShowId,
        refresh: Boolean = true
    ): Flow<Either<NetworkError, Option<Rating>>> = getAllRatedTvShows(refresh).filterData().map { either ->
        either.map { tvShows ->
            tvShows.find { it.tvShow.tmdbId == tvShowId }?.personalRating?.some() ?: none()
        }
    }
}
