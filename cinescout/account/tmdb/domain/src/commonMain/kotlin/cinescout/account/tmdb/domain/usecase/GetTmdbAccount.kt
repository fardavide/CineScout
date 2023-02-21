package cinescout.account.tmdb.domain.usecase

import arrow.core.Either
import cinescout.account.domain.model.Account
import cinescout.account.domain.model.GetAccountError
import cinescout.account.domain.usecase.GetTmdbAccount
import cinescout.account.tmdb.domain.TmdbAccountRepository
import kotlinx.coroutines.flow.Flow
import org.koin.core.annotation.Factory
import store.Refresh

@Factory
class RealGetTmdbAccount(private val accountRepository: TmdbAccountRepository) : GetTmdbAccount {

    override operator fun invoke(refresh: Refresh): Flow<Either<GetAccountError, Account.Tmdb>> =
        accountRepository.getAccount(refresh = refresh)
}
