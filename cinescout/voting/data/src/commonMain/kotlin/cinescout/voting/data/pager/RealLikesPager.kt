package cinescout.voting.data.pager

import app.cash.paging.Pager
import app.cash.paging.PagingConfig
import cinescout.lists.domain.ListSorting
import cinescout.lists.domain.PagingDefaults
import cinescout.screenplay.domain.model.Screenplay
import cinescout.screenplay.domain.model.ScreenplayTypeFilter
import cinescout.voting.domain.pager.LikesPager
import cinescout.voting.domain.repository.VotedScreenplayRepository
import org.koin.core.annotation.Factory

@Factory
class RealLikesPager(
    private val repository: VotedScreenplayRepository
) : LikesPager {

    override fun create(sorting: ListSorting, type: ScreenplayTypeFilter): Pager<Int, Screenplay> = Pager(
        config = PagingConfig(pageSize = PagingDefaults.PageSize),
        pagingSourceFactory = { repository.getPagedLiked(sorting, type) }
    )
}
