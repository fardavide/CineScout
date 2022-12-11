package cinescout.account.trakt.domain.usecase

import cinescout.account.trakt.domain.TraktAccountRepository
import org.koin.core.annotation.Factory

@Factory
class SyncTraktAccount(private val accountRepository: TraktAccountRepository) {

    suspend operator fun invoke() {
        accountRepository.syncAccount()
    }
}
