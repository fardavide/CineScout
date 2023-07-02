package cinescout.progress.domain.usecase

import app.cash.paging.PagingData
import cinescout.lists.domain.ListParams
import cinescout.progress.domain.pager.InProgressPager
import cinescout.screenplay.domain.model.Screenplay
import kotlinx.coroutines.flow.Flow
import org.koin.core.annotation.Factory

interface GetPagedInProgressScreenplays {

    operator fun invoke(param: ListParams): Flow<PagingData<Screenplay>>
}

@Factory
internal class RealGetPagedInProgressScreenplays(
    private val inProgressPager: InProgressPager
) : GetPagedInProgressScreenplays {

    override operator fun invoke(param: ListParams): Flow<PagingData<Screenplay>> =
        inProgressPager.create(param).flow
}
