package cinescout.screenplay.data.remote.datasource

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
import cinescout.screenplay.domain.model.ScreenplayWithGenreSlugs
import cinescout.screenplay.domain.model.id.ScreenplayIds

interface TraktScreenplayRemoteDataSource {

    suspend fun getAllGenres(): Either<NetworkError, Nel<Genre>>

    suspend fun getRecommended(): Either<NetworkOperation, List<ScreenplayIds>>

    suspend fun getScreenplay(ids: ScreenplayIds): Either<NetworkError, ScreenplayWithGenreSlugs>

    suspend fun getSimilar(screenplayIds: ScreenplayIds, page: Int): Either<NetworkError, List<Screenplay>>
}

@CineScoutTestApi
class FakeTraktScreenplayRemoteDataSource(
    private val recommended: List<ScreenplayIds>? = null
) : TraktScreenplayRemoteDataSource {

    override suspend fun getAllGenres(): Either<NetworkError, Nel<Genre>> = notImplementedFake()

    override suspend fun getRecommended(): Either<NetworkOperation, List<ScreenplayIds>> =
        recommended?.right() ?: NetworkOperation.Error(NetworkError.NotFound).left()

    override suspend fun getScreenplay(ids: ScreenplayIds): Either<NetworkError, ScreenplayWithGenreSlugs> =
        notImplementedFake()

    override suspend fun getSimilar(
        screenplayIds: ScreenplayIds,
        page: Int
    ): Either<NetworkError, List<Screenplay>> = notImplementedFake()
}
