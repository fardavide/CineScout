package cinescout.rating.domain.usecase

import cinescout.CineScoutTestApi
import cinescout.notImplementedFake
import cinescout.rating.domain.model.PersonalRatingsStoreKey
import cinescout.rating.domain.model.ScreenplayIdWithPersonalRating
import cinescout.rating.domain.store.PersonalRatingIdsStore
import cinescout.screenplay.domain.model.ScreenplayTypeFilter
import cinescout.store5.StoreFlow
import org.koin.core.annotation.Factory
import org.mobilenativefoundation.store.store5.StoreReadRequest

interface GetPersonalRatingIds {

    operator fun invoke(
        type: ScreenplayTypeFilter,
        refresh: Boolean
    ): StoreFlow<List<ScreenplayIdWithPersonalRating>>
}

@Factory
internal class RealGetPersonalRatingIds(
    private val ratingsStore: PersonalRatingIdsStore
) : GetPersonalRatingIds {

    override operator fun invoke(
        type: ScreenplayTypeFilter,
        refresh: Boolean
    ): StoreFlow<List<ScreenplayIdWithPersonalRating>> {
        val key = PersonalRatingsStoreKey.Read(type)
        return ratingsStore.stream(StoreReadRequest.cached(key, refresh))
    }
}

@CineScoutTestApi
class FakeGetPersonalRatingIds : GetPersonalRatingIds {

    override fun invoke(
        type: ScreenplayTypeFilter,
        refresh: Boolean
    ): StoreFlow<List<ScreenplayIdWithPersonalRating>> = notImplementedFake()
}
