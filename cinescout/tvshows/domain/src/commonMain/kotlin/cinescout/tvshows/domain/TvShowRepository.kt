package cinescout.tvshows.domain

import cinescout.tvshows.domain.model.TmdbTvShowId
import cinescout.tvshows.domain.model.TvShow
import cinescout.tvshows.domain.model.TvShowWithDetails
import store.PagedStore
import store.Paging
import store.Refresh
import store.Store

interface TvShowRepository {

    fun getAllWatchlistTvShows(refresh: Refresh): PagedStore<TvShow, Paging>

    fun getTvShowDetails(id: TmdbTvShowId, refresh: Refresh): Store<TvShowWithDetails>
}
