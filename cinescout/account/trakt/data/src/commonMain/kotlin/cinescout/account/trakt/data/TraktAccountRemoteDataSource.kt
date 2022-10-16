package cinescout.account.trakt.data

import arrow.core.Either
import cinescout.account.trakt.domain.model.TraktAccount
import cinescout.model.NetworkOperation

interface TraktAccountRemoteDataSource {

    suspend fun getAccount(): Either<NetworkOperation, TraktAccount>
}
