package cinescout.rating.domain.usecase

import arrow.core.Either
import cinescout.error.NetworkError
import cinescout.rating.domain.model.ScreenplayPersonalRatingsStoreKey
import cinescout.rating.domain.store.ScreenplayIdPersonalRatingsStore
import cinescout.screenplay.domain.model.Rating
import cinescout.screenplay.domain.model.TmdbScreenplayId
import org.koin.core.annotation.Factory
import org.mobilenativefoundation.store.store5.StoreWriteRequest

@Factory
class RateScreenplay(
    private val store: ScreenplayIdPersonalRatingsStore
) {

    suspend operator fun invoke(id: TmdbScreenplayId, rating: Rating): Either<NetworkError, Unit> {
        val key = ScreenplayPersonalRatingsStoreKey.Write.Add(id, rating)
        return store.write(StoreWriteRequest.of(key, emptyList()))
    }
}
