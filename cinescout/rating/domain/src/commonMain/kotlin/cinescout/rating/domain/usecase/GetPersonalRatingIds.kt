package cinescout.rating.domain.usecase

import cinescout.rating.domain.model.ScreenplayIdWithPersonalRating
import cinescout.rating.domain.model.ScreenplayPersonalRatingsStoreKey
import cinescout.rating.domain.store.ScreenplayIdPersonalRatingsStore
import cinescout.screenplay.domain.model.ScreenplayType
import cinescout.store5.StoreFlow
import org.koin.core.annotation.Factory
import org.mobilenativefoundation.store.store5.StoreReadRequest

interface GetPersonalRatingIds {

    operator fun invoke(
        type: ScreenplayType,
        refresh: Boolean
    ): StoreFlow<List<ScreenplayIdWithPersonalRating>>
}

@Factory
internal class RealGetPersonalRatingIds(
    private val ratingsStore: ScreenplayIdPersonalRatingsStore
) : GetPersonalRatingIds {

    override operator fun invoke(
        type: ScreenplayType,
        refresh: Boolean
    ): StoreFlow<List<ScreenplayIdWithPersonalRating>> {
        val key = ScreenplayPersonalRatingsStoreKey.Read(type)
        return ratingsStore.stream(StoreReadRequest.cached(key, refresh))
    }
}

class FakeGetPersonalRatingIds : GetPersonalRatingIds {

    override fun invoke(
        type: ScreenplayType,
        refresh: Boolean
    ): StoreFlow<List<ScreenplayIdWithPersonalRating>> = TODO()
}