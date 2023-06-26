package cinescout.screenplay.data.remote.datasource

import arrow.core.Either
import cinescout.CineScoutTestApi
import cinescout.error.NetworkError
import cinescout.notImplementedFake
import cinescout.screenplay.domain.model.ScreenplayKeywords
import cinescout.screenplay.domain.model.id.TmdbScreenplayId

interface TmdbScreenplayRemoteDataSource {
    
    suspend fun getScreenplayKeywords(
        screenplayId: TmdbScreenplayId
    ): Either<NetworkError, ScreenplayKeywords>
}

@CineScoutTestApi
class FakeTmdbScreenplayRemoteDataSource : TmdbScreenplayRemoteDataSource {

    override suspend fun getScreenplayKeywords(
        screenplayId: TmdbScreenplayId
    ): Either<NetworkError, ScreenplayKeywords> {
        notImplementedFake()
    }
}
