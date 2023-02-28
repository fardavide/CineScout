package cinescout.tvshows.domain.usecase

import cinescout.tvshows.domain.TvShowRepository
import cinescout.tvshows.domain.model.TvShow
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf
import org.koin.core.annotation.Factory

interface GetAllLikedTvShows {

    operator fun invoke(): Flow<List<TvShow>>
}

@Factory
class RealGetAllLikedTvShows(
    private val tvShowRepository: TvShowRepository
) : GetAllLikedTvShows {

    override operator fun invoke(): Flow<List<TvShow>> = tvShowRepository.getAllLikedTvShows()
}

class FakeGetAllLikedTvShows(
    private val likedTvShows: List<TvShow>? = null
) : GetAllLikedTvShows {

    override operator fun invoke(): Flow<List<TvShow>> = flowOf(likedTvShows ?: emptyList())
}
