package cinescout.tvshows.domain.usecase

import arrow.core.Either
import cinescout.error.DataError
import cinescout.tvshows.domain.TvShowRepository
import cinescout.tvshows.domain.model.TmdbTvShowId
import cinescout.tvshows.domain.model.TvShowCredits
import kotlinx.coroutines.flow.Flow
import org.koin.core.annotation.Factory
import store.Refresh

@Factory
class GetTvShowCredits(
    private val tvShowRepository: TvShowRepository
) {

    operator fun invoke(
        id: TmdbTvShowId,
        refresh: Refresh = Refresh.IfNeeded
    ): Flow<Either<DataError, TvShowCredits>> =
        tvShowRepository.getTvShowCredits(id, refresh)
}
