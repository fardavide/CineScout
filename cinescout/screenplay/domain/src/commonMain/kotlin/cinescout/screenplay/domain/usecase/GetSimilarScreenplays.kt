package cinescout.screenplay.domain.usecase

import cinescout.screenplay.domain.model.Screenplay
import cinescout.screenplay.domain.model.TmdbScreenplayId
import cinescout.screenplay.domain.store.SimilarScreenplaysStore
import cinescout.store5.StoreFlow
import org.koin.core.annotation.Factory
import org.mobilenativefoundation.store.store5.StoreReadRequest

interface GetSimilarScreenplays {

    operator fun invoke(screenplayId: TmdbScreenplayId, refresh: Boolean): StoreFlow<List<Screenplay>>
}

@Factory
internal class RealGetSimilarScreenplays(
    private val store: SimilarScreenplaysStore
) : GetSimilarScreenplays {

    override operator fun invoke(
        screenplayId: TmdbScreenplayId,
        refresh: Boolean
    ): StoreFlow<List<Screenplay>> = store.stream(StoreReadRequest.cached(screenplayId, refresh))
}

class FakeGetSimilarScreenplays : GetSimilarScreenplays {

    override fun invoke(screenplayId: TmdbScreenplayId, refresh: Boolean): StoreFlow<List<Screenplay>> =
        TODO()
}
