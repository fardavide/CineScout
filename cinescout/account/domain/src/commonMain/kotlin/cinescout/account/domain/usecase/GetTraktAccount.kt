package cinescout.account.domain.usecase

import arrow.core.Either
import arrow.core.left
import arrow.core.right
import cinescout.account.domain.AccountRepository
import cinescout.account.domain.model.Account
import cinescout.account.domain.model.GetAccountError
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf
import org.koin.core.annotation.Factory
import store.Refresh

interface GetTraktAccount {

    operator fun invoke(refresh: Refresh = Refresh.WithInterval()): Flow<Either<GetAccountError, Account>>
}

@Factory
class RealGetTraktAccount(private val accountRepository: AccountRepository) :
    GetTraktAccount {

    override operator fun invoke(refresh: Refresh): Flow<Either<GetAccountError, Account>> =
        accountRepository.getAccount(refresh = refresh)
}

class FakeGetTraktAccount(
    account: Account? = null,
    private val result: Either<GetAccountError, Account> = account?.right()
        ?: GetAccountError.NotConnected.left()
) : GetTraktAccount {

    override operator fun invoke(refresh: Refresh): Flow<Either<GetAccountError, Account>> = flowOf(result)
}
