package cinescout.account.domain.usecase

import arrow.core.Either
import arrow.core.left
import arrow.core.right
import cinescout.account.domain.model.Account
import cinescout.account.domain.model.GetAccountError
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf
import store.Refresh

interface GetTmdbAccount {

    operator fun invoke(
        refresh: Refresh = Refresh.WithInterval()
    ): Flow<Either<GetAccountError, Account.Tmdb>>
}

class FakeGetTmdbAccount(
    account: Account.Tmdb? = null,
    private val result: Either<GetAccountError, Account.Tmdb> = account?.right()
        ?: GetAccountError.NoAccountConnected.left()
) : GetTmdbAccount {

    override operator fun invoke(refresh: Refresh): Flow<Either<GetAccountError, Account.Tmdb>> =
        flowOf(result)
}
