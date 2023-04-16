package cinescout.screenplay.data.datasource

import arrow.core.Either
import arrow.core.left
import arrow.core.right
import cinescout.error.NetworkError
import cinescout.model.NetworkOperation
import cinescout.screenplay.domain.model.Screenplay
import cinescout.screenplay.domain.model.ScreenplayGenres
import cinescout.screenplay.domain.model.ScreenplayIds
import cinescout.screenplay.domain.model.ScreenplayKeywords
import cinescout.screenplay.domain.model.TmdbScreenplayId

interface RemoteScreenplayDataSource {

    suspend fun getRecommendedIds(): Either<NetworkOperation, List<ScreenplayIds>>

    suspend fun getScreenplay(screenplayIds: ScreenplayIds): Either<NetworkError, Screenplay>

    suspend fun getScreenplayGenres(screenplayId: TmdbScreenplayId): Either<NetworkError, ScreenplayGenres>
    
    suspend fun getScreenplayKeywords(
        screenplayId: TmdbScreenplayId
    ): Either<NetworkError, ScreenplayKeywords>
    
    suspend fun getSimilar(screenplayIds: ScreenplayIds, page: Int): Either<NetworkError, List<Screenplay>>
}

class FakeRemoteScreenplayDataSource(
    private val hasNetwork: Boolean = true,
    private val recommended: List<ScreenplayIds>? = null
) : RemoteScreenplayDataSource {

    override suspend fun getRecommendedIds(): Either<NetworkOperation, List<ScreenplayIds>> =
        if (hasNetwork) recommended?.right() ?: NetworkOperation.Error(NetworkError.NotFound).left()
        else NetworkOperation.Error(NetworkError.NoNetwork).left()

    override suspend fun getScreenplay(screenplayIds: ScreenplayIds): Either<NetworkError, Screenplay> {
        TODO("Not yet implemented")
    }

    override suspend fun getScreenplayGenres(
        screenplayId: TmdbScreenplayId
    ): Either<NetworkError, ScreenplayGenres> {
        TODO("Not yet implemented")
    }

    override suspend fun getScreenplayKeywords(
        screenplayId: TmdbScreenplayId
    ): Either<NetworkError, ScreenplayKeywords> {
        TODO("Not yet implemented")
    }

    override suspend fun getSimilar(
        screenplayIds: ScreenplayIds,
        page: Int
    ): Either<NetworkError, List<Screenplay>> {
        TODO("Not yet implemented")
    }
}
