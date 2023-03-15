package cinescout.screenplay.data.remote.datasource

import arrow.core.Either
import cinescout.error.NetworkError
import cinescout.screenplay.domain.model.Screenplay
import cinescout.screenplay.domain.model.ScreenplayKeywords
import cinescout.screenplay.domain.model.TmdbScreenplayId

interface TmdbScreenplayRemoteDataSource {
    
    suspend fun getScreenplayKeywords(
        screenplayId: TmdbScreenplayId
    ): Either<NetworkError, ScreenplayKeywords>

    suspend fun getSimilar(screenplayId: TmdbScreenplayId, page: Int): Either<NetworkError, List<Screenplay>>
}

class FakeTmdbScreenplayRemoteDataSource : TmdbScreenplayRemoteDataSource {

    override suspend fun getScreenplayKeywords(
        screenplayId: TmdbScreenplayId
    ): Either<NetworkError, ScreenplayKeywords> {
        TODO("Not yet implemented")
    }

    override suspend fun getSimilar(
        screenplayId: TmdbScreenplayId,
        page: Int
    ): Either<NetworkError, List<Screenplay>> {
        TODO("Not yet implemented")
    }
}
