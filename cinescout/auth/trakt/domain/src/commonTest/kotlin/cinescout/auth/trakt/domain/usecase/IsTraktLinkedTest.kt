package cinescout.auth.trakt.domain.usecase

import cinescout.auth.trakt.domain.TraktAuthRepository
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import kotlin.test.Test
import kotlin.test.assertEquals

class IsTraktLinkedTest {

    private val repository: TraktAuthRepository = mockk {
        coEvery { isLinked() } returns true
    }
    private val isLinked = IsTraktLinked(repository)

    @Test
    fun `does call repository`() = runTest {
        // given
        val expected = true

        // when
        val result = isLinked()

        // then
        assertEquals(expected, result)
        coVerify { repository.isLinked() }
    }
}
