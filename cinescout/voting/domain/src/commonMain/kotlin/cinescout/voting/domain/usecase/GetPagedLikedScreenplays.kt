package cinescout.voting.domain.usecase

import app.cash.paging.PagingData
import cinescout.screenplay.domain.model.Screenplay
import cinescout.screenplay.domain.model.ScreenplayType
import cinescout.voting.domain.pager.LikesPager
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf
import org.koin.core.annotation.Factory

interface GetPagedLikedScreenplays {

    operator fun invoke(type: ScreenplayType): Flow<PagingData<Screenplay>>
}

@Factory
internal class RealGetPagedLikedScreenplays(
    private val likesPager: LikesPager
) : GetPagedLikedScreenplays {

    override operator fun invoke(type: ScreenplayType): Flow<PagingData<Screenplay>> =
        likesPager.create(type).flow
}

class FakeGetPagedLikedScreenplays : GetPagedLikedScreenplays {

    override fun invoke(type: ScreenplayType): Flow<PagingData<Screenplay>> = flowOf(PagingData.empty())
}
