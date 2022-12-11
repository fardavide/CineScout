package cinescout.account.tmdb.domain.usecase

import cinescout.account.tmdb.domain.TmdbAccountRepository
import org.koin.core.annotation.Factory

@Factory
class SyncTmdbAccount(private val accountRepository: TmdbAccountRepository) {

    suspend operator fun invoke() {
        accountRepository.syncAccount()
    }
}
