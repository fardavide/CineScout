package cinescout.progress.domain.usecase

import app.cash.paging.PagingData
import cinescout.CineScoutTestApi
import cinescout.lists.domain.ListParams
import cinescout.progress.domain.pager.InProgressPager
import cinescout.screenplay.domain.model.Screenplay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf
import org.koin.core.annotation.Factory

interface GetPagedInProgressScreenplays {

    operator fun invoke(params: ListParams): Flow<PagingData<Screenplay>>
}

@Factory
internal class RealGetPagedInProgressScreenplays(
    private val inProgressPager: InProgressPager
) : GetPagedInProgressScreenplays {

    override operator fun invoke(params: ListParams): Flow<PagingData<Screenplay>> =
        inProgressPager.create(params).flow
}

@CineScoutTestApi
class FakeGetPagedInProgressScreenplays : GetPagedInProgressScreenplays {

    override fun invoke(params: ListParams): Flow<PagingData<Screenplay>> = flowOf(PagingData.empty())
}
