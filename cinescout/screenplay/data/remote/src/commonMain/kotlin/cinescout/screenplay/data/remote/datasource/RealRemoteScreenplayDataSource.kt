package cinescout.screenplay.data.remote.datasource

import arrow.core.Either
import arrow.core.Nel
import cinescout.error.NetworkError
import cinescout.model.NetworkOperation
import cinescout.screenplay.data.datasource.RemoteScreenplayDataSource
import cinescout.screenplay.domain.model.Genre
import cinescout.screenplay.domain.model.Screenplay
import cinescout.screenplay.domain.model.ScreenplayKeywords
import cinescout.screenplay.domain.model.ScreenplayWithGenreSlugs
import cinescout.screenplay.domain.model.id.ScreenplayIds
import cinescout.screenplay.domain.model.id.TmdbScreenplayId
import org.koin.core.annotation.Factory

@Factory
class RealRemoteScreenplayDataSource(
    private val tmdbSource: TmdbScreenplayRemoteDataSource,
    private val traktSource: TraktScreenplayRemoteDataSource
) : RemoteScreenplayDataSource {

    override suspend fun getAllGenres(): Either<NetworkError, Nel<Genre>> = traktSource.getAllGenres()

    override suspend fun getRecommendedIds(): Either<NetworkOperation, List<ScreenplayIds>> =
        traktSource.getRecommended()

    override suspend fun getScreenplay(
        screenplayIds: ScreenplayIds
    ): Either<NetworkError, ScreenplayWithGenreSlugs> = traktSource.getScreenplay(screenplayIds)

    override suspend fun getScreenplayKeywords(
        screenplayId: TmdbScreenplayId
    ): Either<NetworkError, ScreenplayKeywords> = tmdbSource.getScreenplayKeywords(screenplayId)

    override suspend fun getSimilar(
        screenplayIds: ScreenplayIds,
        page: Int
    ): Either<NetworkError, List<Screenplay>> = traktSource.getSimilar(screenplayIds, page)
}
