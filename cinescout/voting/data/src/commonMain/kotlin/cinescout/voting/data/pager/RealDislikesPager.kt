package cinescout.voting.data.pager

import app.cash.paging.Pager
import app.cash.paging.PagingConfig
import cinescout.lists.domain.ListParams
import cinescout.lists.domain.PagingDefaults
import cinescout.screenplay.domain.model.Screenplay
import cinescout.voting.domain.pager.DislikesPager
import cinescout.voting.domain.repository.VotedScreenplayRepository
import org.koin.core.annotation.Factory

@Factory
class RealDislikesPager(
    private val repository: VotedScreenplayRepository
) : DislikesPager {

    override fun create(params: ListParams): Pager<Int, Screenplay> = Pager(
        config = PagingConfig(pageSize = PagingDefaults.PageSize),
        pagingSourceFactory = { repository.getPagedDisliked(params) }
    )
}
