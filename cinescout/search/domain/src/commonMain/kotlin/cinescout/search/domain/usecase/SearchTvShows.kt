package cinescout.search.domain.usecase

import cinescout.tvshows.domain.TvShowRepository
import cinescout.tvshows.domain.model.TvShow
import store.PagedStore
import store.Paging

class SearchTvShows(
    private val tvShowRepository: TvShowRepository
) {

    operator fun invoke(query: String): PagedStore<TvShow, Paging> =
        tvShowRepository.searchTvShows(query)
}
