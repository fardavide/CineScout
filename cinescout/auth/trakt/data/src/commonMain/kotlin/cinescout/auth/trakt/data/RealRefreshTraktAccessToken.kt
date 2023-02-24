package cinescout.auth.trakt.data

import cinescout.auth.trakt.domain.TraktAuthRepository
import cinescout.network.trakt.RefreshTraktAccessToken
import org.koin.core.annotation.Factory

@Factory
internal class RealRefreshTraktAccessToken(
    private val authRepository: TraktAuthRepository
) : RefreshTraktAccessToken {

    override suspend fun invoke() {
        authRepository.refreshAccessToken()
    }
}
