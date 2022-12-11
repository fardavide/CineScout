package cinescout.tvshows.domain.usecase

import arrow.core.Either
import cinescout.error.DataError
import cinescout.tvshows.domain.TvShowRepository
import cinescout.tvshows.domain.model.TmdbTvShowId
import cinescout.tvshows.domain.model.TvShowWithDetails
import kotlinx.coroutines.flow.Flow
import org.koin.core.annotation.Factory
import store.Refresh
import kotlin.time.Duration.Companion.days

@Factory
class GetTvShowDetails(
    private val tvShowRepository: TvShowRepository
) {

    operator fun invoke(
        tvShowId: TmdbTvShowId,
        refresh: Refresh = Refresh.IfExpired(7.days)
    ): Flow<Either<DataError, TvShowWithDetails>> =
        tvShowRepository.getTvShowDetails(tvShowId, refresh)
}
