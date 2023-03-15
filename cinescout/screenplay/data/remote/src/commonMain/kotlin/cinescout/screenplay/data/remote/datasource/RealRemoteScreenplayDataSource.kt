package cinescout.screenplay.data.remote.datasource

import arrow.core.Either
import cinescout.error.NetworkError
import cinescout.model.NetworkOperation
import cinescout.screenplay.data.datasource.RemoteScreenplayDataSource
import cinescout.screenplay.domain.model.Screenplay
import cinescout.screenplay.domain.model.TmdbScreenplayId
import org.koin.core.annotation.Factory

@Factory
class RealRemoteScreenplayDataSource(
    private val tmdbSource: TmdbScreenplayRemoteDataSource,
    private val traktSource: TraktScreenplayRemoteDataSource
) : RemoteScreenplayDataSource {

    override suspend fun getRecommendedIds(): Either<NetworkOperation, List<TmdbScreenplayId>> =
        traktSource.getRecommended()

    override suspend fun getScreenplay(screenplayId: TmdbScreenplayId): Either<NetworkError, Screenplay> =
        tmdbSource.getScreenplay(screenplayId)

    override suspend fun getSimilar(
        screenplayId: TmdbScreenplayId,
        page: Int
    ): Either<NetworkError, List<Screenplay>> = tmdbSource.getSimilar(screenplayId, page)
}
