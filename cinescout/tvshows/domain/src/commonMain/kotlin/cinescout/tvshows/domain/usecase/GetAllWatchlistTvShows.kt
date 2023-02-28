package cinescout.tvshows.domain.usecase

import cinescout.error.DataError
import cinescout.tvshows.domain.TvShowRepository
import cinescout.tvshows.domain.model.TvShow
import org.koin.core.annotation.Factory
import store.PagedStore
import store.Paging
import store.Refresh
import store.builder.pagedStoreOf
import kotlin.time.Duration.Companion.days

interface GetAllWatchlistTvShows {

    operator fun invoke(refresh: Refresh = Refresh.IfExpired(1.days)): PagedStore<TvShow, Paging>
}

@Factory
class RealGetAllWatchlistTvShows(
    private val tvShowRepository: TvShowRepository
) : GetAllWatchlistTvShows {

    override operator fun invoke(refresh: Refresh): PagedStore<TvShow, Paging> =
        tvShowRepository.getAllWatchlistTvShows(refresh)
}

class FakeGetAllWatchlistTvShows(
    private val watchlist: List<TvShow>? = null
) : GetAllWatchlistTvShows {

    override operator fun invoke(refresh: Refresh): PagedStore<TvShow, Paging> =
        watchlist?.let(::pagedStoreOf) ?: pagedStoreOf(DataError.Local.NoCache)
}
