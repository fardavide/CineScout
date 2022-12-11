package cinescout.suggestions.domain.usecase

import app.cash.turbine.test
import cinescout.auth.tmdb.domain.usecase.IsTmdbLinked
import cinescout.auth.trakt.domain.usecase.IsTraktLinked
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import kotlin.test.Test
import kotlin.test.assertFalse
import kotlin.test.assertTrue

internal class IsLoggedInTest {

    private val isTmdbLinked: IsTmdbLinked = mockk()
    private val isTraktLinked: IsTraktLinked = mockk()
    private val isLoggedIn = IsLoggedIn(isTmdbLinked = isTmdbLinked, isTraktLinked = isTraktLinked)

    @Test
    fun `returns true if Tmdb is linked`() = runTest {
        // given
        every { isTmdbLinked() } returns flowOf(true)
        every { isTraktLinked() } returns flowOf(false)

        // when
        isLoggedIn().test {

            // then
            assertTrue(awaitItem())
            awaitComplete()
        }
    }

    @Test
    fun `returns true if Trakt is linked`() = runTest {
        // given
        every { isTmdbLinked() } returns flowOf(false)
        every { isTraktLinked() } returns flowOf(true)

        // when
        isLoggedIn().test {

            // then
            assertTrue(awaitItem())
            awaitComplete()
        }
    }

    @Test
    fun `returns false if neither Tmdb nor Trakt is linked`() = runTest {
        // given
        every { isTmdbLinked() } returns flowOf(false)
        every { isTraktLinked() } returns flowOf(false)

        // when
        isLoggedIn().test {

            // then
            assertFalse(awaitItem())
            awaitComplete()
        }
    }
}
