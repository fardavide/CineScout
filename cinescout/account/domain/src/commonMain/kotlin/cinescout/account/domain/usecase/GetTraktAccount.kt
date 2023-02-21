package cinescout.account.domain.usecase

import arrow.core.Either
import arrow.core.left
import arrow.core.right
import cinescout.account.domain.model.Account
import cinescout.account.domain.model.GetAccountError
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf
import store.Refresh

interface GetTraktAccount {

    operator fun invoke(
        refresh: Refresh = Refresh.WithInterval()
    ): Flow<Either<GetAccountError, Account.Trakt>>
}

class FakeGetTraktAccount(
    account: Account.Trakt? = null,
    private val result: Either<GetAccountError, Account.Trakt> = account?.right()
        ?: GetAccountError.NotConnected.left()
) : GetTraktAccount {

    override operator fun invoke(refresh: Refresh): Flow<Either<GetAccountError, Account.Trakt>> =
        flowOf(result)
}
