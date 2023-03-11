package cinescout.account.domain.usecase

import arrow.core.Either
import arrow.core.left
import arrow.core.right
import cinescout.account.domain.model.Account
import cinescout.account.domain.model.GetAccountError
import cinescout.account.domain.store.AccountStore
import cinescout.account.domain.store.withAccountError
import cinescout.store5.stream
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf
import org.koin.core.annotation.Factory

interface GetTraktAccount {

    operator fun invoke(refresh: Boolean): Flow<Either<GetAccountError, Account>>
}

@Factory
class RealGetTraktAccount(private val accountStore: AccountStore) : GetTraktAccount {

    override operator fun invoke(refresh: Boolean): Flow<Either<GetAccountError, Account>> =
        accountStore.stream(refresh = refresh).withAccountError()
}

class FakeGetTraktAccount(
    account: Account? = null,
    private val result: Either<GetAccountError, Account> = account?.right()
        ?: GetAccountError.NotConnected.left()
) : GetTraktAccount {

    override operator fun invoke(refresh: Boolean): Flow<Either<GetAccountError, Account>> = flowOf(result)
}
