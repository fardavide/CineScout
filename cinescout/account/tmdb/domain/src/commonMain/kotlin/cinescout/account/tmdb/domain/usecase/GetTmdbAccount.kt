package cinescout.account.tmdb.domain.usecase

import arrow.core.Either
import cinescout.account.domain.model.GetAccountError
import cinescout.account.tmdb.domain.TmdbAccountRepository
import cinescout.account.tmdb.domain.model.TmdbAccount
import kotlinx.coroutines.flow.Flow

class GetTmdbAccount(private val accountRepository: TmdbAccountRepository) {

    operator fun invoke(): Flow<Either<GetAccountError, TmdbAccount>> =
        accountRepository.getAccount()
}
