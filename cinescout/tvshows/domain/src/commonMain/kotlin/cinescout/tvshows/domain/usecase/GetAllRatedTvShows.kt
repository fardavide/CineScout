package cinescout.tvshows.domain.usecase

import cinescout.error.NetworkError
import cinescout.store5.StoreFlow
import cinescout.store5.stream
import cinescout.store5.test.storeFlowOf
import cinescout.tvshows.domain.model.TvShowWithPersonalRating
import cinescout.tvshows.domain.store.RatedTvShowsStore
import org.koin.core.annotation.Factory

interface GetAllRatedTvShows {

    operator fun invoke(refresh: Boolean = true): StoreFlow<List<TvShowWithPersonalRating>>
}

@Factory
class RealGetAllRatedTvShows(
    private val ratedTvShowsStore: RatedTvShowsStore
) : GetAllRatedTvShows {

    override operator fun invoke(refresh: Boolean): StoreFlow<List<TvShowWithPersonalRating>> =
        ratedTvShowsStore.stream(refresh = refresh)
}

class FakeGetAllRatedTvShows(
    private val ratedTvShows: List<TvShowWithPersonalRating>? = null
) : GetAllRatedTvShows {

    override operator fun invoke(refresh: Boolean): StoreFlow<List<TvShowWithPersonalRating>> =
        ratedTvShows?.let(::storeFlowOf) ?: storeFlowOf(NetworkError.NotFound)
}
