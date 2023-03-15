package cinescout.rating.domain.usecase

import arrow.core.Either
import arrow.core.Option
import arrow.core.firstOrNone
import cinescout.error.NetworkError
import cinescout.rating.domain.model.ScreenplayPersonalRatingsStoreKey
import cinescout.rating.domain.store.ScreenplayIdPersonalRatingsStore
import cinescout.screenplay.domain.model.Rating
import cinescout.screenplay.domain.model.ScreenplayType
import cinescout.screenplay.domain.model.TmdbScreenplayId
import cinescout.store5.ext.filterData
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import org.koin.core.annotation.Factory
import org.mobilenativefoundation.store.store5.StoreReadRequest

@Factory
class GetPersonalRating(
    private val ratingsStore: ScreenplayIdPersonalRatingsStore
) {
    
    operator fun invoke(
        screenplayId: TmdbScreenplayId,
        refresh: Boolean
    ): Flow<Either<NetworkError, Option<Rating>>> {
        val key = ScreenplayPersonalRatingsStoreKey.Read(ScreenplayType.All)
        return ratingsStore.stream(StoreReadRequest.cached(key, refresh)).filterData().map { ratingsEither ->
            ratingsEither.map { ratings ->
                ratings.firstOrNone { it.screenplayId == screenplayId }.map { it.personalRating }
            }
        }
    }
}
