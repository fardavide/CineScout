package cinescout.screenplay.data.remote.datasource

import arrow.core.Either
import cinescout.CineScoutTestApi
import cinescout.error.NetworkError
import cinescout.notImplementedFake
import cinescout.screenplay.domain.model.ScreenplayGenres
import cinescout.screenplay.domain.model.ScreenplayKeywords
import cinescout.screenplay.domain.model.ids.TmdbScreenplayId

interface TmdbScreenplayRemoteDataSource {
    
    suspend fun getScreenplayKeywords(
        screenplayId: TmdbScreenplayId
    ): Either<NetworkError, ScreenplayKeywords>

    suspend fun getScreenplayGenres(screenplayId: TmdbScreenplayId): Either<NetworkError, ScreenplayGenres>
}

@CineScoutTestApi
class FakeTmdbScreenplayRemoteDataSource : TmdbScreenplayRemoteDataSource {

    override suspend fun getScreenplayKeywords(
        screenplayId: TmdbScreenplayId
    ): Either<NetworkError, ScreenplayKeywords> {
        notImplementedFake()
    }

    override suspend fun getScreenplayGenres(
        screenplayId: TmdbScreenplayId
    ): Either<NetworkError, ScreenplayGenres> {
        notImplementedFake()
    }

}
