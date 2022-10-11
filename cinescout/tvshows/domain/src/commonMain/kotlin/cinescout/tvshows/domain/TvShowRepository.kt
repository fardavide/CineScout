package cinescout.tvshows.domain

import cinescout.tvshows.domain.model.TvShow
import store.PagedStore
import store.Paging
import store.Refresh

interface TvShowRepository {

    fun getAllWatchlistTvShows(refresh: Refresh): PagedStore<TvShow, Paging>
}
