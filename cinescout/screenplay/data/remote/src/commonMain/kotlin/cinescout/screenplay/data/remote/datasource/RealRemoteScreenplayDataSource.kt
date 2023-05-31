package cinescout.screenplay.data.remote.datasource

import arrow.core.Either
import cinescout.error.NetworkError
import cinescout.model.NetworkOperation
import cinescout.screenplay.data.datasource.RemoteScreenplayDataSource
import cinescout.screenplay.domain.model.Screenplay
import cinescout.screenplay.domain.model.ScreenplayGenres
import cinescout.screenplay.domain.model.ScreenplayKeywords
import cinescout.screenplay.domain.model.ids.ScreenplayIds
import cinescout.screenplay.domain.model.ids.TmdbScreenplayId
import org.koin.core.annotation.Factory

@Factory
class RealRemoteScreenplayDataSource(
    private val tmdbSource: TmdbScreenplayRemoteDataSource,
    private val traktSource: TraktScreenplayRemoteDataSource
) : RemoteScreenplayDataSource {

    override suspend fun getRecommendedIds(): Either<NetworkOperation, List<ScreenplayIds>> =
        traktSource.getRecommended()

    override suspend fun getScreenplay(screenplayIds: ScreenplayIds): Either<NetworkError, Screenplay> =
        traktSource.getScreenplay(screenplayIds)

    override suspend fun getScreenplayGenres(
        screenplayId: TmdbScreenplayId
    ): Either<NetworkError, ScreenplayGenres> = tmdbSource.getScreenplayGenres(screenplayId)

    override suspend fun getScreenplayKeywords(
        screenplayId: TmdbScreenplayId
    ): Either<NetworkError, ScreenplayKeywords> = tmdbSource.getScreenplayKeywords(screenplayId)

    override suspend fun getSimilar(
        screenplayIds: ScreenplayIds,
        page: Int
    ): Either<NetworkError, List<Screenplay>> = traktSource.getSimilar(screenplayIds, page)
}
