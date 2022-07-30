package cinescout.account.tmdb.data

import arrow.core.Either
import cinescout.account.tmdb.domain.model.TmdbAccount
import cinescout.error.NetworkError

interface TmdbAccountRemoteDataSource {

    suspend fun getAccount(): Either<NetworkError, TmdbAccount>
}
