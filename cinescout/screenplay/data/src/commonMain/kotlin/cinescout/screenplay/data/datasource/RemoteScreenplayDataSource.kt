package cinescout.screenplay.data.datasource

import arrow.core.Either
import arrow.core.Nel
import arrow.core.left
import arrow.core.right
import cinescout.CineScoutTestApi
import cinescout.error.NetworkError
import cinescout.model.NetworkOperation
import cinescout.notImplementedFake
import cinescout.screenplay.domain.model.Genre
import cinescout.screenplay.domain.model.Screenplay
import cinescout.screenplay.domain.model.ScreenplayKeywords
import cinescout.screenplay.domain.model.ScreenplayWithGenreSlugs
import cinescout.screenplay.domain.model.id.ScreenplayIds
import cinescout.screenplay.domain.model.id.TmdbScreenplayId

interface RemoteScreenplayDataSource {

    suspend fun getAllGenres(): Either<NetworkError, Nel<Genre>>

    suspend fun getRecommendedIds(): Either<NetworkOperation, List<ScreenplayIds>>

    suspend fun getScreenplay(screenplayIds: ScreenplayIds): Either<NetworkError, ScreenplayWithGenreSlugs>

    suspend fun getScreenplayKeywords(
        screenplayId: TmdbScreenplayId
    ): Either<NetworkError, ScreenplayKeywords>

    suspend fun getSimilar(screenplayIds: ScreenplayIds, page: Int): Either<NetworkError, List<Screenplay>>
}

@CineScoutTestApi
class FakeRemoteScreenplayDataSource(
    private val hasNetwork: Boolean = true,
    private val recommended: List<ScreenplayIds>? = null
) : RemoteScreenplayDataSource {

    override suspend fun getAllGenres(): Either<NetworkError, Nel<Genre>> = notImplementedFake()

    override suspend fun getRecommendedIds(): Either<NetworkOperation, List<ScreenplayIds>> =
        if (hasNetwork) recommended?.right() ?: NetworkOperation.Error(NetworkError.NotFound).left()
        else NetworkOperation.Error(NetworkError.NoNetwork).left()

    override suspend fun getScreenplay(
        screenplayIds: ScreenplayIds
    ): Either<NetworkError, ScreenplayWithGenreSlugs> = notImplementedFake()

    override suspend fun getScreenplayKeywords(
        screenplayId: TmdbScreenplayId
    ): Either<NetworkError, ScreenplayKeywords> = notImplementedFake()

    override suspend fun getSimilar(
        screenplayIds: ScreenplayIds,
        page: Int
    ): Either<NetworkError, List<Screenplay>> = notImplementedFake()
}
