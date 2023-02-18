package cinescout.account.tmdb.data

import arrow.core.Either
import arrow.core.left
import arrow.core.right
import cinescout.account.tmdb.domain.model.TmdbAccount
import cinescout.error.NetworkError
import cinescout.model.NetworkOperation

interface TmdbAccountRemoteDataSource {

    suspend fun getAccount(): Either<NetworkOperation, TmdbAccount>
}

class FakeTmdbAccountRemoteDataSource(
    private val account: TmdbAccount? = null,
    private val networkError: NetworkError = NetworkError.Unauthorized,
    private val accountEither: Either<NetworkOperation, TmdbAccount> =
        account?.right() ?: NetworkOperation.Error(networkError).left()
) : TmdbAccountRemoteDataSource {

    override suspend fun getAccount(): Either<NetworkOperation, TmdbAccount> = accountEither
}
