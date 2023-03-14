package cinescout.rating.domain.usecase

import arrow.core.Either
import arrow.core.Option
import arrow.core.firstOrNone
import cinescout.error.NetworkError
import cinescout.rating.domain.store.ScreenplayIdPersonalRatingsStore
import cinescout.screenplay.domain.model.Rating
import cinescout.screenplay.domain.model.TmdbScreenplayId
import cinescout.store5.ext.filterData
import cinescout.store5.stream
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import org.koin.core.annotation.Factory

@Factory
class GetPersonalRating(
    private val ratingsStore: ScreenplayIdPersonalRatingsStore
) {
    
    operator fun invoke(
        screenplayId: TmdbScreenplayId,
        refresh: Boolean
    ): Flow<Either<NetworkError, Option<Rating>>> =
        ratingsStore.stream(refresh).filterData().map { ratingsEither ->
            ratingsEither.map { ratings ->
                ratings.firstOrNone { it.screenplayId == screenplayId }.map { it.personalRating }
            }
        }
}
