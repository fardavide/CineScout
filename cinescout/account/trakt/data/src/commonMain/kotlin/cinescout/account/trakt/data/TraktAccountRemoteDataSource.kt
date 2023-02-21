package cinescout.account.trakt.data

import arrow.core.Either
import arrow.core.left
import arrow.core.right
import cinescout.account.domain.model.Account
import cinescout.error.NetworkError
import cinescout.model.NetworkOperation

interface TraktAccountRemoteDataSource {

    suspend fun getAccount(): Either<NetworkOperation, Account.Trakt>
}

class FakeTraktAccountRemoteDataSource(
    private val account: Account.Trakt? = null,
    private val networkError: NetworkError = NetworkError.Unauthorized,
    private val accountEither: Either<NetworkOperation, Account.Trakt> =
        account?.right() ?: NetworkOperation.Error(networkError).left()
) : TraktAccountRemoteDataSource {

    override suspend fun getAccount(): Either<NetworkOperation, Account.Trakt> = accountEither
}
