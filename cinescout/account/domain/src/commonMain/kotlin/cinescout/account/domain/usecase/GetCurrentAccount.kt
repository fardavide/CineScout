package cinescout.account.domain.usecase

import arrow.core.Either
import arrow.core.handleErrorWith
import arrow.core.left
import arrow.core.right
import cinescout.account.domain.model.Account
import cinescout.account.domain.model.GetAccountError
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flowOf
import org.koin.core.annotation.Factory
import store.Refresh

interface GetCurrentAccount {

    operator fun invoke(refresh: Refresh = Refresh.WithInterval()): Flow<Either<GetAccountError, Account>>
}

@Factory
class RealGetCurrentAccount(
    private val getTmdbAccount: GetTmdbAccount,
    private val getTraktAccount: GetTraktAccount
) : GetCurrentAccount {

    override operator fun invoke(refresh: Refresh): Flow<Either<GetAccountError, Account>> = combine(
        getTmdbAccount(refresh = refresh),
        getTraktAccount(refresh = refresh)
    ) { tmdbAccountEither, traktAccountEither ->
        check(tmdbAccountEither.isLeft() || traktAccountEither.isLeft()) {
            "Both accounts are connected: this is not supported"
        }
        tmdbAccountEither.handleErrorWith { tmdbError ->
            traktAccountEither.handleErrorWith { traktError ->
                if (tmdbError is GetAccountError.Network) {
                    tmdbError.left()
                } else {
                    traktError.left()
                }
            }
        }
    }
}

class FakeGetCurrentAccount(
    account: Account? = null,
    val result: Either<GetAccountError, Account> = account?.right() ?: GetAccountError.NotConnected.left()
) : GetCurrentAccount {

    override operator fun invoke(refresh: Refresh): Flow<Either<GetAccountError, Account>> = flowOf(result)
}
