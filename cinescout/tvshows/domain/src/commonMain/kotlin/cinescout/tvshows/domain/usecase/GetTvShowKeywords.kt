package cinescout.tvshows.domain.usecase

import arrow.core.Either
import cinescout.error.DataError
import cinescout.tvshows.domain.TvShowRepository
import cinescout.tvshows.domain.model.TmdbTvShowId
import cinescout.tvshows.domain.model.TvShowKeywords
import kotlinx.coroutines.flow.Flow
import store.Refresh

class GetTvShowKeywords(
    private val tvShowRepository: TvShowRepository
) {

    operator fun invoke(
        tvShowId: TmdbTvShowId,
        refresh: Refresh = Refresh.IfNeeded
    ): Flow<Either<DataError, TvShowKeywords>> =
        tvShowRepository.getTvShowKeywords(tvShowId, refresh)
}
