package cinescout.auth.tmdb.domain.usecase

import cinescout.account.tmdb.domain.TmdbAccountRepository
import cinescout.auth.tmdb.domain.repository.TmdbAuthRepository
import org.koin.core.annotation.Factory

interface UnlinkFromTmdb {

    suspend operator fun invoke()
}

@Factory
class RealUnlinkFromTmdb(
    private val tmdbAccountRepository: TmdbAccountRepository,
    private val tmdbAuthRepository: TmdbAuthRepository
) : UnlinkFromTmdb {

    override suspend operator fun invoke() {
        tmdbAuthRepository.unlink()
        tmdbAccountRepository.removeAccount()
    }
}

class FakeUnlinkFromTmdb : UnlinkFromTmdb {

    var invoked = false
        private set

    override suspend operator fun invoke() {
        invoked = true
    }
}
