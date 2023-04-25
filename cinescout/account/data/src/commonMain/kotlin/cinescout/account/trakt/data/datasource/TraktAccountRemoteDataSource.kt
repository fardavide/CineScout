package cinescout.account.trakt.data.datasource

import arrow.core.Either
import arrow.core.left
import arrow.core.right
import cinescout.account.domain.model.Account
import cinescout.error.NetworkError
import cinescout.model.NetworkOperation

interface TraktAccountRemoteDataSource {

    suspend fun getAccount(): Either<NetworkOperation, Account>
}

class FakeTraktAccountRemoteDataSource(
    private val account: Account? = null,
    private val networkError: NetworkError = NetworkError.Unauthorized,
    private val accountEither: Either<NetworkOperation, Account> =
        account?.right() ?: NetworkOperation.Error(networkError).left()
) : TraktAccountRemoteDataSource {

    override suspend fun getAccount(): Either<NetworkOperation, Account> = accountEither
}
