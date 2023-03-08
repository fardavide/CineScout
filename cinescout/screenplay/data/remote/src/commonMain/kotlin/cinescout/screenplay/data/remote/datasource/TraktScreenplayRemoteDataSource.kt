package cinescout.screenplay.data.remote.datasource

import arrow.core.Either
import arrow.core.left
import arrow.core.right
import cinescout.error.NetworkError
import cinescout.model.NetworkOperation
import cinescout.screenplay.domain.model.TmdbScreenplayId

interface TraktScreenplayRemoteDataSource {

    suspend fun getRecommended(): Either<NetworkOperation, List<TmdbScreenplayId>>
}

class FakeTraktScreenplayRemoteDataSource(
    private val recommended: List<TmdbScreenplayId>? = null
) : TraktScreenplayRemoteDataSource {

    override suspend fun getRecommended(): Either<NetworkOperation, List<TmdbScreenplayId>> =
        recommended?.right() ?: NetworkOperation.Error(NetworkError.NotFound).left()
}
