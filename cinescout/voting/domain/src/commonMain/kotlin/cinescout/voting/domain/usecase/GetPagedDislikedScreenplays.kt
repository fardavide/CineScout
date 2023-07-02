package cinescout.voting.domain.usecase

import app.cash.paging.PagingData
import cinescout.lists.domain.ListParams
import cinescout.screenplay.domain.model.Screenplay
import cinescout.voting.domain.pager.DislikesPager
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf
import org.koin.core.annotation.Factory

interface GetPagedDislikedScreenplays {

    operator fun invoke(params: ListParams): Flow<PagingData<Screenplay>>
}

@Factory
internal class RealGetPagedDislikedScreenplays(
    private val dislikesPager: DislikesPager
) : GetPagedDislikedScreenplays {

    override operator fun invoke(params: ListParams): Flow<PagingData<Screenplay>> =
        dislikesPager.create(params).flow
}

class FakeGetPagedDislikedScreenplays : GetPagedDislikedScreenplays {

    override fun invoke(params: ListParams): Flow<PagingData<Screenplay>> = flowOf(PagingData.empty())
}
