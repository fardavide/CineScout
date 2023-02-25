package cinescout.account.domain.usecase

import arrow.core.Either
import arrow.core.left
import arrow.core.right
import cinescout.account.domain.model.Account
import cinescout.account.domain.model.GetAccountError
import cinescout.auth.domain.usecase.IsTmdbLinked
import cinescout.auth.domain.usecase.IsTraktLinked
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOf
import org.koin.core.annotation.Factory
import store.Refresh

interface GetCurrentAccount {

    operator fun invoke(refresh: Refresh = Refresh.WithInterval()): Flow<Either<GetAccountError, Account>>
}

@Factory
class RealGetCurrentAccount(
    private val getTmdbAccount: GetTmdbAccount,
    private val getTraktAccount: GetTraktAccount,
    private val isTmdbLinked: IsTmdbLinked,
    private val isTraktLinked: IsTraktLinked
) : GetCurrentAccount {

    override operator fun invoke(refresh: Refresh): Flow<Either<GetAccountError, Account>> = combine(
        isTmdbLinked(),
        isTraktLinked()
    ) { (isTmdbLinked, isTraktLinked) ->
        check(isTmdbLinked.not() || isTraktLinked.not()) {
            "Both accounts are connected: this is not supported"
        }
        isTmdbLinked to isTraktLinked
    }.flatMapLatest { (isTmdbLinked, isTraktLinked) ->
        combine(
            getTmdbAccount(refresh = refresh),
            getTraktAccount(refresh = refresh)
        ) { tmdbAccountEither, traktAccountEither ->
            when {
                isTmdbLinked -> tmdbAccountEither
                isTraktLinked -> traktAccountEither
                else -> GetAccountError.NotConnected.left()
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
