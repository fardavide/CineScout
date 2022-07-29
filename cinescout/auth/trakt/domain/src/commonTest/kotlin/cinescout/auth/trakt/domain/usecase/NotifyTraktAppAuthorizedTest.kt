package cinescout.auth.trakt.domain.usecase

import cinescout.auth.trakt.domain.TraktAuthRepository
import cinescout.auth.trakt.domain.testdata.TraktTestData
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import kotlin.test.Test

class NotifyTraktAppAuthorizedTest {

    private val authRepository: TraktAuthRepository = mockk(relaxUnitFun = true)
    private val notify = NotifyTraktAppAuthorized(authRepository)

    @Test
    fun `calls repository`() = runTest {
        notify(TraktTestData.AuthorizationCode)
        coVerify { authRepository.notifyAppAuthorized(TraktTestData.AuthorizationCode) }
    }
}
