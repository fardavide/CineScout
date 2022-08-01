package cinescout.account.trakt.data

import arrow.core.Either
import cinescout.account.trakt.domain.model.TraktAccount
import cinescout.error.NetworkError

interface TraktAccountRemoteDataSource {

    suspend fun getAccount(): Either<NetworkError, TraktAccount>
}
