package screenplay.data.remote.trakt.datasource

import arrow.core.Either
import cinescout.auth.domain.usecase.CallWithTraktAccount
import cinescout.model.NetworkOperation
import cinescout.screenplay.data.remote.datasource.TraktScreenplayRemoteDataSource
import cinescout.screenplay.domain.model.TmdbScreenplayId
import cinescout.utils.kotlin.plus
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
import org.koin.core.annotation.Factory
import screenplay.data.remote.trakt.service.TraktRecommendationService

@Factory
class RealTraktScreenplayRemoteDataSource(
    private val callWithTraktAccount: CallWithTraktAccount,
    private val service: TraktRecommendationService
) : TraktScreenplayRemoteDataSource {

    override suspend fun getRecommended(): Either<NetworkOperation, List<TmdbScreenplayId>> =
        callWithTraktAccount {
            coroutineScope {
                val moviesDeferred = async { service.getRecommendedMovies().map { list -> list.map { it.ids.tmdb } } }
                val tvShowsDeferred = async { service.getRecommendedTvShows().map { list -> list.map { it.ids.tmdb } } }
                moviesDeferred.await() + tvShowsDeferred.await()
            }
        }
}
