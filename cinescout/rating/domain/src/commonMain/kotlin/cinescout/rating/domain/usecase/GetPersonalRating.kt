package cinescout.rating.domain.usecase

import arrow.core.Either
import arrow.core.Option
import arrow.core.firstOrNone
import arrow.core.left
import arrow.core.right
import cinescout.error.NetworkError
import cinescout.rating.domain.model.PersonalRatingsStoreKey
import cinescout.rating.domain.store.PersonalRatingIdsStore
import cinescout.screenplay.domain.model.Rating
import cinescout.screenplay.domain.model.ScreenplayTypeFilter
import cinescout.screenplay.domain.model.TmdbScreenplayId
import cinescout.store5.ext.filterData
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.map
import org.jetbrains.annotations.VisibleForTesting
import org.koin.core.annotation.Factory
import org.mobilenativefoundation.store.store5.StoreReadRequest

interface GetPersonalRating {

    operator fun invoke(
        screenplayId: TmdbScreenplayId,
        refresh: Boolean
    ): Flow<Either<NetworkError, Option<Rating>>>
}

@Factory
class RealGetPersonalRating(
    private val ratingsStore: PersonalRatingIdsStore
) : GetPersonalRating {

    override operator fun invoke(
        screenplayId: TmdbScreenplayId,
        refresh: Boolean
    ): Flow<Either<NetworkError, Option<Rating>>> {
        val key = PersonalRatingsStoreKey.Read(ScreenplayTypeFilter.All)
        return ratingsStore.stream(StoreReadRequest.cached(key, refresh)).filterData().map { ratingsEither ->
            ratingsEither.map { ratings ->
                ratings.firstOrNone {
                    it.screenplayIds.tmdb == screenplayId
                }.map { it.personalRating }
            }
        }
    }
}

@VisibleForTesting
class FakeGetPersonalRating(
    private val rating: Option<Rating>? = null
) : GetPersonalRating {

    override operator fun invoke(
        screenplayId: TmdbScreenplayId,
        refresh: Boolean
    ): Flow<Either<NetworkError, Option<Rating>>> = flowOf(rating?.right() ?: NetworkError.Unknown.left())
}
