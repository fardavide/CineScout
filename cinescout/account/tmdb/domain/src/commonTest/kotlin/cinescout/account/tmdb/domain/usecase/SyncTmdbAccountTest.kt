package cinescout.account.tmdb.domain.usecase

import cinescout.account.tmdb.domain.TmdbAccountRepository
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import kotlin.test.Test

class SyncTmdbAccountTest {

    private val repository: TmdbAccountRepository = mockk(relaxUnitFun = true)
    private val sync = SyncTmdbAccount(repository)

    @Test
    fun `does call repository`() = runTest {
        sync()
        coVerify { repository.syncAccount() }
    }
}
