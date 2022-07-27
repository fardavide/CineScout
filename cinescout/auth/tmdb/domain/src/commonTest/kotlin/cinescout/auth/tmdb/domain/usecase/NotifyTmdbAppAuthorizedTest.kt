package cinescout.auth.tmdb.domain.usecase

import cinescout.auth.tmdb.domain.TmdbAuthRepository
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import kotlin.test.Test

class NotifyTmdbAppAuthorizedTest {

    private val authRepository: TmdbAuthRepository = mockk(relaxUnitFun = true)
    private val notify = NotifyTmdbAppAuthorized(authRepository)

    @Test
    fun `calls repository`() = runTest {
        notify()
        coVerify { authRepository.notifyTokenAuthorized() }
    }
}
