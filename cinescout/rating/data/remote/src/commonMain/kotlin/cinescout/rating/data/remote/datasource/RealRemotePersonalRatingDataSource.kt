package cinescout.rating.data.remote.datasource

import arrow.core.Either
import cinescout.auth.domain.usecase.CallWithTraktAccount
import cinescout.model.NetworkOperation
import cinescout.rating.data.datasource.RemotePersonalRatingDataSource
import cinescout.rating.data.remote.mapper.TraktRatingsMapper
import cinescout.rating.data.remote.service.TraktRatingService
import cinescout.rating.domain.model.ScreenplayIdWithPersonalRating
import cinescout.screenplay.domain.model.Rating
import cinescout.screenplay.domain.model.ScreenplayType
import cinescout.screenplay.domain.model.TmdbScreenplayId
import org.koin.core.annotation.Factory
import screenplay.data.remote.trakt.mapper.TraktScreenplayMetadataMapper

@Factory
internal class RealRemotePersonalRatingDataSource(
    private val callWithTraktAccount: CallWithTraktAccount,
    private val metadataMapper: TraktScreenplayMetadataMapper,
    private val ratingsMapper: TraktRatingsMapper,
    private val traktRatingService: TraktRatingService
) : RemotePersonalRatingDataSource {

    override suspend fun getRatingIds(
        type: ScreenplayType
    ): Either<NetworkOperation, List<ScreenplayIdWithPersonalRating>> = callWithTraktAccount {
        traktRatingService.getRatings(type).map(ratingsMapper::toScreenplayIds)
    }

    override suspend fun postRating(
        screenplayId: TmdbScreenplayId,
        rating: Rating
    ): Either<NetworkOperation, Unit> = callWithTraktAccount {
        traktRatingService.postAddRating(ratingsMapper.toRequest(screenplayId, rating))
    }

    override suspend fun deleteRating(screenplayId: TmdbScreenplayId): Either<NetworkOperation, Unit> =
        callWithTraktAccount {
            traktRatingService.postRemoveRating(metadataMapper.toMultiRequest(screenplayId))
        }
}
