package cinescout.screenplay.data.remote.datasource

import arrow.core.Either
import cinescout.model.NetworkOperation
import cinescout.screenplay.data.datasource.RemoteScreenplayDataSource
import cinescout.screenplay.domain.model.TmdbScreenplayId
import org.koin.core.annotation.Factory

@Factory
class RealRemoteScreenplayDataSource(
    private val traktSource: TraktScreenplayRemoteDataSource
) : RemoteScreenplayDataSource {

    override suspend fun getRecommended(): Either<NetworkOperation, List<TmdbScreenplayId>> =
        traktSource.getRecommended()
}
