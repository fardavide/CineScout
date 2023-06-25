package cinescout.voting.data.pager

import app.cash.paging.Pager
import app.cash.paging.PagingConfig
import arrow.core.Option
import cinescout.lists.domain.ListSorting
import cinescout.lists.domain.PagingDefaults
import cinescout.screenplay.domain.model.Screenplay
import cinescout.screenplay.domain.model.ScreenplayTypeFilter
import cinescout.screenplay.domain.model.TmdbGenreId
import cinescout.voting.domain.pager.DislikesPager
import cinescout.voting.domain.repository.VotedScreenplayRepository
import org.koin.core.annotation.Factory

@Factory
class RealDislikesPager(
    private val repository: VotedScreenplayRepository
) : DislikesPager {

    override fun create(
        genreFilter: Option<TmdbGenreId>,
        sorting: ListSorting,
        type: ScreenplayTypeFilter
    ): Pager<Int, Screenplay> = Pager(
        config = PagingConfig(pageSize = PagingDefaults.PageSize),
        pagingSourceFactory = { repository.getPagedDisliked(genreFilter, sorting, type) }
    )
}
