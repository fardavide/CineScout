package cinescout.auth.trakt.domain.usecase

import cinescout.auth.trakt.domain.TraktAuthRepository
import cinescout.auth.trakt.domain.model.TraktAuthorizationCode

class NotifyTraktAppAuthorized(
    private val authRepository: TraktAuthRepository
) {

    suspend operator fun invoke(code: TraktAuthorizationCode) {
        authRepository.notifyAppAuthorized(code)
    }
}
