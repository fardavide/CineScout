package cinescout.account.tmdb.domain.usecase

import arrow.core.Either
import arrow.core.left
import arrow.core.right
import cinescout.account.domain.model.Account
import cinescout.account.domain.model.GetAccountError
import cinescout.account.tmdb.domain.TmdbAccountRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf
import org.koin.core.annotation.Factory
import store.Refresh

interface GetTmdbAccount {

    operator fun invoke(
        refresh: Refresh = Refresh.WithInterval()
    ): Flow<Either<GetAccountError, Account.Tmdb>>
}

@Factory
class RealGetTmdbAccount(private val accountRepository: TmdbAccountRepository) : GetTmdbAccount {

    override operator fun invoke(refresh: Refresh): Flow<Either<GetAccountError, Account.Tmdb>> =
        accountRepository.getAccount(refresh = refresh)
}

class FakeGetTmdbAccount(private val account: Account.Tmdb? = null) : GetTmdbAccount {

    override operator fun invoke(refresh: Refresh): Flow<Either<GetAccountError, Account.Tmdb>> =
        flowOf(account?.right() ?: GetAccountError.NoAccountConnected.left())
}
