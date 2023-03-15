package cinescout.voting.data.pager

import app.cash.paging.Pager
import app.cash.paging.PagingConfig
import cinescout.screenplay.domain.model.Screenplay
import cinescout.screenplay.domain.model.ScreenplayType
import cinescout.voting.domain.pager.LikesPager
import cinescout.voting.domain.repository.VotedScreenplayRepository
import org.koin.core.annotation.Factory

@Factory
class RealLikesPager(
    private val repository: VotedScreenplayRepository
) : LikesPager {

    override fun create(listType: ScreenplayType): Pager<Int, Screenplay> = Pager(
        config = PagingConfig(pageSize = 50),
        pagingSourceFactory = { repository.getPagedLiked(listType) }
    )
}
