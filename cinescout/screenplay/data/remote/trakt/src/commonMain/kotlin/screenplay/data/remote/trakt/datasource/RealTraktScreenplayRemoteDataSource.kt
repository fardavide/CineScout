package screenplay.data.remote.trakt.datasource

import arrow.core.Either
import cinescout.auth.domain.usecase.CallWithTraktAccount
import cinescout.error.NetworkError
import cinescout.model.NetworkOperation
import cinescout.screenplay.data.remote.datasource.TraktScreenplayRemoteDataSource
import cinescout.screenplay.domain.model.Screenplay
import cinescout.screenplay.domain.model.ScreenplayIds
import cinescout.utils.kotlin.plus
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
import org.koin.core.annotation.Factory
import screenplay.data.remote.trakt.mapper.TraktScreenplayMapper
import screenplay.data.remote.trakt.service.TraktRecommendationService
import screenplay.data.remote.trakt.service.TraktScreenplayService

@Factory
class RealTraktScreenplayRemoteDataSource(
    private val callWithTraktAccount: CallWithTraktAccount,
    private val recommendationService: TraktRecommendationService,
    private val screenplayMapper: TraktScreenplayMapper,
    private val screenplayService: TraktScreenplayService
) : TraktScreenplayRemoteDataSource {

    override suspend fun getRecommended(): Either<NetworkOperation, List<ScreenplayIds>> =
        callWithTraktAccount {
            coroutineScope {
                val moviesDeferred =
                    async { recommendationService.getRecommendedMovies().map { list -> list.map { it.ids.ids } } }
                val tvShowsDeferred =
                    async { recommendationService.getRecommendedTvShows().map { list -> list.map { it.ids.ids } } }
                moviesDeferred.await() + tvShowsDeferred.await()
            }
        }

    override suspend fun getScreenplay(ids: ScreenplayIds): Either<NetworkError, Screenplay> =
        screenplayService.getScreenplay(ids.trakt).map(screenplayMapper::toScreenplay)

    override suspend fun getSimilar(
        screenplayIds: ScreenplayIds,
        page: Int
    ): Either<NetworkError, List<Screenplay>> =
        screenplayService.getSimilar(screenplayIds.trakt, page).map(screenplayMapper::toScreenplays)
}
