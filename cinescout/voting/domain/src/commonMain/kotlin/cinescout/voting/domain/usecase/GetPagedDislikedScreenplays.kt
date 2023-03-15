package cinescout.voting.domain.usecase

import app.cash.paging.PagingData
import cinescout.screenplay.domain.model.Screenplay
import cinescout.screenplay.domain.model.ScreenplayType
import cinescout.voting.domain.pager.DislikesPager
import kotlinx.coroutines.flow.Flow
import org.koin.core.annotation.Factory

interface GetPagedDislikedScreenplays {

    operator fun invoke(type: ScreenplayType): Flow<PagingData<Screenplay>>
}

@Factory
internal class RealGetPagedDislikedScreenplays(
    private val dislikesPager: DislikesPager
) : GetPagedDislikedScreenplays {

    override operator fun invoke(type: ScreenplayType): Flow<PagingData<Screenplay>> =
        dislikesPager.create(type).flow
}
