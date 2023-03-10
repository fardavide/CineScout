package cinescout.tvshows.domain.usecase

import cinescout.error.NetworkError
import cinescout.store5.StoreFlow
import cinescout.store5.test.storeFlowOf
import cinescout.tvshows.domain.TvShowRepository
import cinescout.tvshows.domain.model.TvShowWithPersonalRating
import org.koin.core.annotation.Factory

interface GetAllRatedTvShows {

    operator fun invoke(refresh: Boolean = true): StoreFlow<List<TvShowWithPersonalRating>>
}

@Factory
class RealGetAllRatedTvShows(
    private val tvShowRepository: TvShowRepository
) : GetAllRatedTvShows {

    override operator fun invoke(refresh: Boolean): StoreFlow<List<TvShowWithPersonalRating>> =
        tvShowRepository.getAllRatedTvShows(refresh)
}

class FakeGetAllRatedTvShows(
    private val ratedTvShows: List<TvShowWithPersonalRating>? = null
) : GetAllRatedTvShows {

    override operator fun invoke(refresh: Boolean): StoreFlow<List<TvShowWithPersonalRating>> =
        ratedTvShows?.let(::storeFlowOf) ?: storeFlowOf(NetworkError.NotFound)
}
