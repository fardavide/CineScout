package cinescout.voting.domain.usecase

import app.cash.paging.PagingData
import cinescout.lists.domain.ListSorting
import cinescout.screenplay.domain.model.Screenplay
import cinescout.screenplay.domain.model.ScreenplayTypeFilter
import cinescout.voting.domain.pager.DislikesPager
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf
import org.koin.core.annotation.Factory

interface GetPagedDislikedScreenplays {

    operator fun invoke(sorting: ListSorting, type: ScreenplayTypeFilter): Flow<PagingData<Screenplay>>
}

@Factory
internal class RealGetPagedDislikedScreenplays(
    private val dislikesPager: DislikesPager
) : GetPagedDislikedScreenplays {

    override operator fun invoke(
        sorting: ListSorting,
        type: ScreenplayTypeFilter
    ): Flow<PagingData<Screenplay>> = dislikesPager.create(sorting, type).flow
}

class FakeGetPagedDislikedScreenplays : GetPagedDislikedScreenplays {

    override fun invoke(sorting: ListSorting, type: ScreenplayTypeFilter): Flow<PagingData<Screenplay>> =
        flowOf(PagingData.empty())
}
