package cinescout.account.trakt.domain.usecase

import arrow.core.Either
import cinescout.account.domain.model.GetAccountError
import cinescout.account.trakt.domain.TraktAccountRepository
import cinescout.account.trakt.domain.model.TraktAccount
import kotlinx.coroutines.flow.Flow

class GetTraktAccount(private val accountRepository: TraktAccountRepository) {

    operator fun invoke(): Flow<Either<GetAccountError, TraktAccount>> =
        accountRepository.getAccount()
}
