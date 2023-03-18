package cinescout.rating.domain.usecase

import arrow.core.Either
import cinescout.error.NetworkError
import cinescout.rating.domain.model.PersonalRatingsStoreKey
import cinescout.rating.domain.store.PersonalRatingIdsStore
import cinescout.screenplay.domain.model.Rating
import cinescout.screenplay.domain.model.TmdbScreenplayId
import org.koin.core.annotation.Factory
import org.mobilenativefoundation.store.store5.StoreWriteRequest

@Factory
class RateScreenplay(
    private val store: PersonalRatingIdsStore
) {

    suspend operator fun invoke(id: TmdbScreenplayId, rating: Rating): Either<NetworkError, Unit> {
        val key = PersonalRatingsStoreKey.Write.Add(id, rating)
        return store.write(StoreWriteRequest.of(key, emptyList()))
    }
}
