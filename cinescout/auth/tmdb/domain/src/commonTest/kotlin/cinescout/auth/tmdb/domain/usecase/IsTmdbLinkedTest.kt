package cinescout.auth.tmdb.domain.usecase

import app.cash.turbine.test
import cinescout.auth.tmdb.domain.TmdbAuthRepository
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import kotlin.test.Test
import kotlin.test.assertEquals

class IsTmdbLinkedTest {

    private val repository: TmdbAuthRepository = mockk {
        coEvery { isLinked() } returns flowOf(true)
    }
    private val isLinked = IsTmdbLinked(repository)

    @Test
    fun `does call repository`() = runTest {
        // given
        val expected = true

        // when
        isLinked().test {

            // then
            assertEquals(expected, awaitItem())
            awaitComplete()
            coEvery { repository.isLinked() }
        }
    }
}
