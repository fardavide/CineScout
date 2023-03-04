package cinescout.tvshows.domain.usecase

import cinescout.tvshows.domain.TvShowRepository
import cinescout.tvshows.domain.model.TmdbTvShowId
import org.koin.core.annotation.Factory

interface AddTvShowToLikedList {

    suspend operator fun invoke(tvShowId: TmdbTvShowId)
}

@Factory
class RealAddTvShowToLikedList(
    private val tvShowRepository: TvShowRepository
) : AddTvShowToLikedList {

    override suspend operator fun invoke(tvShowId: TmdbTvShowId) {
        tvShowRepository.addToLiked(tvShowId)
    }
}

class FakeAddTvShowToLikedList : AddTvShowToLikedList {

    override suspend operator fun invoke(tvShowId: TmdbTvShowId) {
        // no-op
    }
}
