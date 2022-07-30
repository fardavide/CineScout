package cinescout.account.tmdb.domain.usecase

import cinescout.account.tmdb.domain.TmdbAccountRepository

class SyncTmdbAccount(private val accountRepository: TmdbAccountRepository) {

    suspend operator fun invoke() {
        accountRepository.syncAccount()
    }
}
