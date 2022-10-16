package cinescout.account.tmdb.data

import arrow.core.Either
import cinescout.account.tmdb.domain.model.TmdbAccount
import cinescout.model.NetworkOperation

interface TmdbAccountRemoteDataSource {

    suspend fun getAccount(): Either<NetworkOperation, TmdbAccount>
}
