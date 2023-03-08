package cinescout.screenplay.data.datasource

import arrow.core.Either
import arrow.core.left
import arrow.core.right
import cinescout.error.NetworkError
import cinescout.model.NetworkOperation
import cinescout.screenplay.domain.model.TmdbScreenplayId

interface RemoteScreenplayDataSource {

    suspend fun getRecommended(): Either<NetworkOperation, List<TmdbScreenplayId>>
}

class FakeRemoteScreenplayDataSource(
    private val hasNetwork: Boolean = true,
    private val recommended: List<TmdbScreenplayId>? = null
) : RemoteScreenplayDataSource {

    override suspend fun getRecommended(): Either<NetworkOperation, List<TmdbScreenplayId>> =
        if (hasNetwork) recommended?.right() ?: NetworkOperation.Error(NetworkError.NotFound).left()
        else NetworkOperation.Error(NetworkError.NoNetwork).left()

}
