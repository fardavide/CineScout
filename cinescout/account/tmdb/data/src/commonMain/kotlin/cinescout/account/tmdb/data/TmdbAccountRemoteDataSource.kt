package cinescout.account.tmdb.data

import arrow.core.Either
import arrow.core.left
import arrow.core.right
import cinescout.account.domain.model.Account
import cinescout.error.NetworkError
import cinescout.model.NetworkOperation

interface TmdbAccountRemoteDataSource {

    suspend fun getAccount(): Either<NetworkOperation, Account.Tmdb>
}

class FakeTmdbAccountRemoteDataSource(
    private val account: Account.Tmdb? = null,
    private val networkError: NetworkError = NetworkError.Unauthorized,
    private val accountEither: Either<NetworkOperation, Account.Tmdb> =
        account?.right() ?: NetworkOperation.Error(networkError).left()
) : TmdbAccountRemoteDataSource {

    override suspend fun getAccount(): Either<NetworkOperation, Account.Tmdb> = accountEither
}
