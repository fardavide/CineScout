package cinescout.rating.domain.usecase

import arrow.core.Either
import cinescout.error.NetworkError
import cinescout.rating.domain.model.ScreenplayIdWithPersonalRating
import cinescout.rating.domain.store.ScreenplayIdPersonalRatingsStore
import cinescout.screenplay.domain.model.Rating
import cinescout.screenplay.domain.model.TmdbScreenplayId
import cinescout.store5.write
import org.koin.core.annotation.Factory

@Factory
class RateScreenplay(
    private val store: ScreenplayIdPersonalRatingsStore
) {

    suspend operator fun invoke(id: TmdbScreenplayId, rating: Rating): Either<NetworkError, Unit> =
        store.write(listOf(ScreenplayIdWithPersonalRating(id, rating)))
}
