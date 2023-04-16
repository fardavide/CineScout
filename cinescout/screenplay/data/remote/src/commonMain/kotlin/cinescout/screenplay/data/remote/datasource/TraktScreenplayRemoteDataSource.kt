package cinescout.screenplay.data.remote.datasource

import arrow.core.Either
import arrow.core.left
import arrow.core.right
import cinescout.error.NetworkError
import cinescout.model.NetworkOperation
import cinescout.screenplay.domain.model.Screenplay
import cinescout.screenplay.domain.model.ScreenplayIds

interface TraktScreenplayRemoteDataSource {

    suspend fun getRecommended(): Either<NetworkOperation, List<ScreenplayIds>>

    suspend fun getScreenplay(ids: ScreenplayIds): Either<NetworkError, Screenplay>
    
    suspend fun getSimilar(screenplayIds: ScreenplayIds, page: Int): Either<NetworkError, List<Screenplay>>
}

class FakeTraktScreenplayRemoteDataSource(
    private val recommended: List<ScreenplayIds>? = null
) : TraktScreenplayRemoteDataSource {

    override suspend fun getRecommended(): Either<NetworkOperation, List<ScreenplayIds>> =
        recommended?.right() ?: NetworkOperation.Error(NetworkError.NotFound).left()

    override suspend fun getScreenplay(ids: ScreenplayIds): Either<NetworkError, Screenplay> {
        TODO("Not yet implemented")
    }

    override suspend fun getSimilar(
        screenplayIds: ScreenplayIds,
        page: Int
    ): Either<NetworkError, List<Screenplay>> {
        TODO("Not yet implemented")
    }
}
