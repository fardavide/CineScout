package cinescout.tvshows.domain.usecase

import cinescout.error.DataError
import cinescout.tvshows.domain.TvShowRepository
import cinescout.tvshows.domain.model.TvShow
import org.koin.core.annotation.Factory
import store.Refresh
import store.Store
import store.builder.listStoreOf
import store.builder.storeOf
import kotlin.time.Duration.Companion.days

interface GetAllWatchlistTvShows {

    operator fun invoke(refresh: Refresh = Refresh.IfExpired(1.days)): Store<List<TvShow>>
}

@Factory
class RealGetAllWatchlistTvShows(
    private val tvShowRepository: TvShowRepository
) : GetAllWatchlistTvShows {

    override operator fun invoke(refresh: Refresh): Store<List<TvShow>> =
        tvShowRepository.getAllWatchlistTvShows(refresh)
}

class FakeGetAllWatchlistTvShows(
    private val watchlist: List<TvShow>? = null
) : GetAllWatchlistTvShows {

    override operator fun invoke(refresh: Refresh): Store<List<TvShow>> =
        watchlist?.let(::listStoreOf) ?: storeOf(DataError.Local.NoCache)
}
