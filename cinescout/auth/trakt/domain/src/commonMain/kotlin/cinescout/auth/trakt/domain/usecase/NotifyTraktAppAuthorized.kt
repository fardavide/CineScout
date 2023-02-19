package cinescout.auth.trakt.domain.usecase

import cinescout.auth.trakt.domain.TraktAuthRepository
import cinescout.auth.trakt.domain.model.TraktAuthorizationCode
import org.koin.core.annotation.Factory

interface NotifyTraktAppAuthorized {

    suspend operator fun invoke(code: TraktAuthorizationCode)
}

@Factory
class RealNotifyTraktAppAuthorized(
    private val authRepository: TraktAuthRepository
) : NotifyTraktAppAuthorized {

    override suspend operator fun invoke(code: TraktAuthorizationCode) {
        authRepository.notifyAppAuthorized(code)
    }
}

class FakeNotifyTraktAppAuthorized : NotifyTraktAppAuthorized {

    var invokedWithAuthorizationCode: TraktAuthorizationCode? = null
        private set

    override suspend operator fun invoke(code: TraktAuthorizationCode) {
        invokedWithAuthorizationCode = code
    }
}
