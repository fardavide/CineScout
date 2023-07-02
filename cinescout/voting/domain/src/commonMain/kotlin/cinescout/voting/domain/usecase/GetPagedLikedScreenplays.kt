package cinescout.voting.domain.usecase

import app.cash.paging.PagingData
import cinescout.lists.domain.ListParams
import cinescout.screenplay.domain.model.Screenplay
import cinescout.voting.domain.pager.LikesPager
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf
import org.koin.core.annotation.Factory

interface GetPagedLikedScreenplays {

    operator fun invoke(params: ListParams): Flow<PagingData<Screenplay>>
}

@Factory
internal class RealGetPagedLikedScreenplays(
    private val likesPager: LikesPager
) : GetPagedLikedScreenplays {

    override operator fun invoke(params: ListParams): Flow<PagingData<Screenplay>> =
        likesPager.create(params).flow
}

class FakeGetPagedLikedScreenplays : GetPagedLikedScreenplays {

    override fun invoke(params: ListParams): Flow<PagingData<Screenplay>> = flowOf(PagingData.empty())
}
