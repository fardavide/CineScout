package cinescout.account.trakt.domain.usecase

import arrow.core.Either
import arrow.core.left
import arrow.core.right
import cinescout.account.domain.model.Account
import cinescout.account.domain.model.GetAccountError
import cinescout.account.trakt.domain.TraktAccountRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf
import org.koin.core.annotation.Factory
import store.Refresh

interface GetTraktAccount {

    operator fun invoke(
        refresh: Refresh = Refresh.WithInterval()
    ): Flow<Either<GetAccountError, Account.Trakt>>
}

@Factory
class RealGetTraktAccount(private val accountRepository: TraktAccountRepository) : GetTraktAccount {

    override operator fun invoke(refresh: Refresh): Flow<Either<GetAccountError, Account.Trakt>> =
        accountRepository.getAccount(refresh = refresh)
}

class FakeGetTraktAccount(private val account: Account.Trakt? = null) : GetTraktAccount {

    override operator fun invoke(refresh: Refresh): Flow<Either<GetAccountError, Account.Trakt>> =
        flowOf(account?.right() ?: GetAccountError.NoAccountConnected.left())
}
