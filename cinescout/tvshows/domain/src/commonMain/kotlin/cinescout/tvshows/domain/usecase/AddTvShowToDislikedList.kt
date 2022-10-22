package cinescout.tvshows.domain.usecase

import cinescout.tvshows.domain.TvShowRepository
import cinescout.tvshows.domain.model.TmdbTvShowId

class AddTvShowToDislikedList(
    private val tvShowRepository: TvShowRepository
) {

    suspend operator fun invoke(tvShowId: TmdbTvShowId) {
        tvShowRepository.addToDisliked(tvShowId)
    }
}
