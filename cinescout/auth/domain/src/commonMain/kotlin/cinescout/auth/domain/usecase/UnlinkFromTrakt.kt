package cinescout.auth.domain.usecase

import cinescout.auth.domain.TraktAuthRepository
import org.koin.core.annotation.Factory

interface UnlinkFromTrakt {

    suspend operator fun invoke()
}

@Factory
class RealUnlinkFromTrakt(
    private val traktAuthRepository: TraktAuthRepository
) : UnlinkFromTrakt {

    override suspend operator fun invoke() {
        traktAuthRepository.unlink()
    }
}

class FakeUnlinkFromTrakt : UnlinkFromTrakt {

    var invoked = false
        private set

    override suspend operator fun invoke() {
        invoked = true
    }
}
