package cinescout.screenplay.data.remote.datasource

import arrow.core.Either
import arrow.core.left
import arrow.core.right
import cinescout.error.NetworkError
import cinescout.model.NetworkOperation
import cinescout.screenplay.domain.model.Screenplay
import cinescout.screenplay.domain.model.ScreenplayIds
import cinescout.screenplay.domain.model.TraktScreenplayId

interface TraktScreenplayRemoteDataSource {

    suspend fun getRecommended(): Either<NetworkOperation, List<ScreenplayIds>>

    suspend fun getScreenplay(id: TraktScreenplayId): Either<NetworkError, Screenplay>
}

class FakeTraktScreenplayRemoteDataSource(
    private val recommended: List<ScreenplayIds>? = null
) : TraktScreenplayRemoteDataSource {

    override suspend fun getRecommended(): Either<NetworkOperation, List<ScreenplayIds>> =
        recommended?.right() ?: NetworkOperation.Error(NetworkError.NotFound).left()

    override suspend fun getScreenplay(id: TraktScreenplayId): Either<NetworkError, Screenplay> {
        TODO("Not yet implemented")
    }
}
