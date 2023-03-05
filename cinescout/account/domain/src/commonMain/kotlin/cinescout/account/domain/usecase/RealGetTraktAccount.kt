package cinescout.account.domain.usecase

import arrow.core.Either
import cinescout.account.domain.TraktAccountRepository
import cinescout.account.domain.model.Account
import cinescout.account.domain.model.GetAccountError
import kotlinx.coroutines.flow.Flow
import org.koin.core.annotation.Factory
import store.Refresh

@Factory
class RealGetTraktAccount(private val accountRepository: TraktAccountRepository) :
    GetTraktAccount {

    override operator fun invoke(refresh: Refresh): Flow<Either<GetAccountError, Account>> =
        accountRepository.getAccount(refresh = refresh)
}
