package cinescout.auth.trakt.domain.usecase

import cinescout.account.trakt.domain.TraktAccountRepository
import cinescout.auth.trakt.domain.TraktAuthRepository
import org.koin.core.annotation.Factory

interface UnlinkFromTrakt {

    suspend operator fun invoke()
}

@Factory
class RealUnlinkFromTrakt(
    private val traktAccountRepository: TraktAccountRepository,
    private val traktAuthRepository: TraktAuthRepository
) : UnlinkFromTrakt {

    override suspend operator fun invoke() {
        traktAuthRepository.unlink()
        traktAccountRepository.removeAccount()
    }
}

class FakeUnlinkFromTrakt : UnlinkFromTrakt {

    var invoked = false
        private set

    override suspend operator fun invoke() {
        invoked = true
    }
}
