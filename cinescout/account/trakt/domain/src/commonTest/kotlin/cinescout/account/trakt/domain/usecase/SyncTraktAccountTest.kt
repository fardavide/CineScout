package cinescout.account.trakt.domain.usecase

import cinescout.account.trakt.domain.TraktAccountRepository
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import kotlin.test.Test

class SyncTraktAccountTest {

    private val repository: TraktAccountRepository = mockk(relaxUnitFun = true)
    private val sync = SyncTraktAccount(repository)

    @Test
    fun `does call repository`() = runTest {
        sync()
        coVerify { repository.syncAccount() }
    }
}
