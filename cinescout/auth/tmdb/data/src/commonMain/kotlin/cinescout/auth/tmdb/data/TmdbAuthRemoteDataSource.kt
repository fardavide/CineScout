package cinescout.auth.tmdb.data

import arrow.core.Either
import cinescout.auth.tmdb.data.model.TmdbAccessToken
import cinescout.auth.tmdb.data.model.TmdbAuthorizedRequestToken
import cinescout.auth.tmdb.data.model.TmdbRequestToken
import cinescout.error.NetworkError

interface TmdbAuthRemoteDataSource {

    suspend fun createRequestToken(): Either<NetworkError, TmdbRequestToken>

    suspend fun createAccessToken(token: TmdbAuthorizedRequestToken): Either<NetworkError, TmdbAccessToken>
}
