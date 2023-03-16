package cinescout.account.domain.usecase

import arrow.core.Either
import arrow.core.left
import arrow.core.right
import cinescout.account.domain.model.Account
import cinescout.account.domain.model.GetAccountError
import cinescout.auth.domain.usecase.IsTraktLinked
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOf
import org.koin.core.annotation.Factory

interface GetCurrentAccount {

    operator fun invoke(refresh: Boolean): Flow<Either<GetAccountError, Account>>
}

@Factory
class RealGetCurrentAccount(
    private val getTraktAccount: GetTraktAccount,
    private val isTraktLinked: IsTraktLinked
) : GetCurrentAccount {

    override operator fun invoke(refresh: Boolean): Flow<Either<GetAccountError, Account>> =
        isTraktLinked().flatMapLatest { isTraktLinked ->
            when (isTraktLinked) {
                true -> getTraktAccount(refresh = refresh)
                false -> flowOf(GetAccountError.NotConnected.left())
            }
        }
}

class FakeGetCurrentAccount(
    account: Account? = null,
    val result: Either<GetAccountError, Account> = account?.right() ?: GetAccountError.NotConnected.left()
) : GetCurrentAccount {

    override operator fun invoke(refresh: Boolean): Flow<Either<GetAccountError, Account>> = flowOf(result)
}
