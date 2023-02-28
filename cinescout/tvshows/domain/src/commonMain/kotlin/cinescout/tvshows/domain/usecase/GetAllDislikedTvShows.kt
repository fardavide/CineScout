package cinescout.tvshows.domain.usecase

import cinescout.tvshows.domain.TvShowRepository
import cinescout.tvshows.domain.model.TvShow
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf
import org.koin.core.annotation.Factory

interface GetAllDislikedTvShows {

    operator fun invoke(): Flow<List<TvShow>>
}

@Factory
class RealGetAllDislikedTvShows(
    private val tvShowRepository: TvShowRepository
) : GetAllDislikedTvShows {

    override operator fun invoke(): Flow<List<TvShow>> = tvShowRepository.getAllDislikedTvShows()
}

class FakeGetAllDislikedTvShows(
    private val dislikedTvShows: List<TvShow>? = null
) : GetAllDislikedTvShows {

    override operator fun invoke(): Flow<List<TvShow>> = flowOf(dislikedTvShows ?: emptyList())
}
