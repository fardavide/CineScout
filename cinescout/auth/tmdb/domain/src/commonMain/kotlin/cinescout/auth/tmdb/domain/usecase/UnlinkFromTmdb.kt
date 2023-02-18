package cinescout.auth.tmdb.domain.usecase

import cinescout.account.tmdb.domain.TmdbAccountRepository
import cinescout.auth.tmdb.domain.repository.TmdbAuthRepository
import org.koin.core.annotation.Factory

@Factory
class UnlinkFromTmdb(
    private val tmdbAccountRepository: TmdbAccountRepository,
    private val tmdbAuthRepository: TmdbAuthRepository
) {

    suspend operator fun invoke() {
        tmdbAuthRepository.unlink()
        tmdbAccountRepository.removeAccount()
    }
}
