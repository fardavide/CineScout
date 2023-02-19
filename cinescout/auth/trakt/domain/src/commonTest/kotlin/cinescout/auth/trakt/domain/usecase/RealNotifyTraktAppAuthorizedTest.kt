package cinescout.auth.trakt.domain.usecase

import cinescout.auth.trakt.domain.TraktAuthRepository
import cinescout.auth.trakt.domain.sample.TraktAuthorizationCodeSample
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import kotlin.test.Test

class RealNotifyTraktAppAuthorizedTest {

    private val authRepository: TraktAuthRepository = mockk(relaxUnitFun = true)
    private val notify = RealNotifyTraktAppAuthorized(authRepository)

    @Test
    fun `calls repository`() = runTest {
        notify(TraktAuthorizationCodeSample.AuthorizationCode)
        coVerify { authRepository.notifyAppAuthorized(TraktAuthorizationCodeSample.AuthorizationCode) }
    }
}
