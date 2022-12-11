package cinescout.account.trakt.domain.usecase

import arrow.core.Either
import cinescout.account.domain.model.GetAccountError
import cinescout.account.trakt.domain.TraktAccountRepository
import cinescout.account.trakt.domain.model.TraktAccount
import kotlinx.coroutines.flow.Flow
import org.koin.core.annotation.Factory
import store.Refresh

@Factory
class GetTraktAccount(private val accountRepository: TraktAccountRepository) {

    operator fun invoke(refresh: Refresh = Refresh.WithInterval()): Flow<Either<GetAccountError, TraktAccount>> =
        accountRepository.getAccount(refresh = refresh)
}
