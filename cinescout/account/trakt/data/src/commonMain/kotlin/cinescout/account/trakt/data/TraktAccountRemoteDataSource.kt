package cinescout.account.trakt.data

import arrow.core.Either
import arrow.core.left
import arrow.core.right
import cinescout.account.trakt.domain.model.TraktAccount
import cinescout.error.NetworkError
import cinescout.model.NetworkOperation

interface TraktAccountRemoteDataSource {

    suspend fun getAccount(): Either<NetworkOperation, TraktAccount>
}

class FakeTraktAccountRemoteDataSource(
    private val account: TraktAccount? = null,
    private val networkError: NetworkError = NetworkError.Unauthorized,
    private val accountEither: Either<NetworkOperation, TraktAccount> =
        account?.right() ?: NetworkOperation.Error(networkError).left()
) : TraktAccountRemoteDataSource {

    override suspend fun getAccount(): Either<NetworkOperation, TraktAccount> = accountEither
}
