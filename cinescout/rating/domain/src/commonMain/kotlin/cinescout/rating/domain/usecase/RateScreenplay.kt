package cinescout.rating.domain.usecase

import arrow.core.Either
import cinescout.error.NetworkError
import cinescout.rating.domain.model.PersonalRatingsStoreKey
import cinescout.rating.domain.store.PersonalRatingIdsStore
import cinescout.screenplay.domain.model.Rating
import cinescout.screenplay.domain.model.TmdbScreenplayId
import org.koin.core.annotation.Factory
import org.mobilenativefoundation.store.store5.StoreWriteRequest

interface RateScreenplay {

    suspend operator fun invoke(id: TmdbScreenplayId, rating: Rating): Either<NetworkError, Unit>
}

@Factory
internal class RealRateScreenplay(
    private val store: PersonalRatingIdsStore
) : RateScreenplay {

    override suspend operator fun invoke(id: TmdbScreenplayId, rating: Rating): Either<NetworkError, Unit> {
        val key = PersonalRatingsStoreKey.Write.Add(id, rating)
        return store.write(StoreWriteRequest.of(key, emptyList()))
    }
}

class FakeRateScreenplay : RateScreenplay {

    override suspend operator fun invoke(id: TmdbScreenplayId, rating: Rating): Either<NetworkError, Unit> =
        throw NotImplementedError()
}
