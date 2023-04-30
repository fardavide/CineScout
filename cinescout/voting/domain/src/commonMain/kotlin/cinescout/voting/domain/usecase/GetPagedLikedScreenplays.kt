package cinescout.voting.domain.usecase

import app.cash.paging.PagingData
import cinescout.lists.domain.ListSorting
import cinescout.screenplay.domain.model.Screenplay
import cinescout.screenplay.domain.model.ScreenplayTypeFilter
import cinescout.voting.domain.pager.LikesPager
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf
import org.koin.core.annotation.Factory

interface GetPagedLikedScreenplays {

    operator fun invoke(sorting: ListSorting, type: ScreenplayTypeFilter): Flow<PagingData<Screenplay>>
}

@Factory
internal class RealGetPagedLikedScreenplays(
    private val likesPager: LikesPager
) : GetPagedLikedScreenplays {

    override operator fun invoke(
        sorting: ListSorting,
        type: ScreenplayTypeFilter
    ): Flow<PagingData<Screenplay>> = likesPager.create(sorting, type).flow
}

class FakeGetPagedLikedScreenplays : GetPagedLikedScreenplays {

    override fun invoke(sorting: ListSorting, type: ScreenplayTypeFilter): Flow<PagingData<Screenplay>> =
        flowOf(PagingData.empty())
}
