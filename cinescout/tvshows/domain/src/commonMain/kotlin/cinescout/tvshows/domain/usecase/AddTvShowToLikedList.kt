package cinescout.tvshows.domain.usecase

import cinescout.tvshows.domain.TvShowRepository
import cinescout.tvshows.domain.model.TmdbTvShowId
import org.koin.core.annotation.Factory

@Factory
class AddTvShowToLikedList(
    private val tvShowRepository: TvShowRepository
) {

    suspend operator fun invoke(tvShowId: TmdbTvShowId) {
        tvShowRepository.addToLiked(tvShowId)
    }
}
