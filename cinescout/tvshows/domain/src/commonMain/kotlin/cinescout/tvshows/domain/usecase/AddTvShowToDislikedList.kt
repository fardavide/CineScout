package cinescout.tvshows.domain.usecase

import cinescout.tvshows.domain.TvShowRepository
import cinescout.tvshows.domain.model.TmdbTvShowId
import org.koin.core.annotation.Factory

interface AddTvShowToDislikedList {

    suspend operator fun invoke(tvShowId: TmdbTvShowId)
}

@Factory
class RealAddTvShowToDislikedList(
    private val tvShowRepository: TvShowRepository
) : AddTvShowToDislikedList {

    override suspend operator fun invoke(tvShowId: TmdbTvShowId) {
        tvShowRepository.addToDisliked(tvShowId)
    }
}

class FakeAddTvShowToDislikedList : AddTvShowToDislikedList {

    override suspend operator fun invoke(tvShowId: TmdbTvShowId) {
        // no-op
    }
}
