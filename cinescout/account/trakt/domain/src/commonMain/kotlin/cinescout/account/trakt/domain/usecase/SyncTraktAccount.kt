package cinescout.account.trakt.domain.usecase

import cinescout.account.trakt.domain.TraktAccountRepository

class SyncTraktAccount(private val accountRepository: TraktAccountRepository) {

    suspend operator fun invoke() {
        accountRepository.syncAccount()
    }
}
