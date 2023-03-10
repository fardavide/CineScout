package cinescout.tvshows.domain.usecase

import cinescout.error.NetworkError
import cinescout.store5.StoreFlow
import cinescout.store5.test.storeFlowOf
import cinescout.tvshows.domain.TvShowRepository
import cinescout.tvshows.domain.model.TvShow
import org.koin.core.annotation.Factory

interface GetAllWatchlistTvShows {

    operator fun invoke(refresh: Boolean = true): StoreFlow<List<TvShow>>
}

@Factory
class RealGetAllWatchlistTvShows(
    private val tvShowRepository: TvShowRepository
) : GetAllWatchlistTvShows {

    override operator fun invoke(refresh: Boolean): StoreFlow<List<TvShow>> =
        tvShowRepository.getAllWatchlistTvShows(refresh)
}

class FakeGetAllWatchlistTvShows(
    private val watchlist: List<TvShow>? = null
) : GetAllWatchlistTvShows {

    override operator fun invoke(refresh: Boolean): StoreFlow<List<TvShow>> =
        watchlist?.let(::storeFlowOf) ?: storeFlowOf(NetworkError.NotFound)
}
