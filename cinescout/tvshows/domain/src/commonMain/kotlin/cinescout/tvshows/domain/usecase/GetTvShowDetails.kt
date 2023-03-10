package cinescout.tvshows.domain.usecase

import cinescout.store5.StoreFlow
import cinescout.tvshows.domain.TvShowRepository
import cinescout.tvshows.domain.model.TmdbTvShowId
import cinescout.tvshows.domain.model.TvShowWithDetails
import org.koin.core.annotation.Factory

@Factory
class GetTvShowDetails(
    private val tvShowRepository: TvShowRepository
) {

    operator fun invoke(tvShowId: TmdbTvShowId, refresh: Boolean = true): StoreFlow<TvShowWithDetails> =
        tvShowRepository.getTvShowDetails(tvShowId, refresh)
}
