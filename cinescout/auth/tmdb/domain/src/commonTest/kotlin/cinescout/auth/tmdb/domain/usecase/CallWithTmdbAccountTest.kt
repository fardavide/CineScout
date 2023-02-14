package cinescout.auth.tmdb.domain.usecase

import arrow.core.left
import arrow.core.right
import cinescout.error.NetworkError
import cinescout.model.NetworkOperation
import cinescout.test.kotlin.TestTimeoutMs
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.TestScope
import kotlinx.coroutines.test.runTest
import kotlin.test.Test
import kotlin.test.assertEquals

internal class CallWithTmdbAccountTest {

    private val appScope = TestScope()
    private val isTmdbLinked: IsTmdbLinked = mockk()
    private val callWithTmdbAccount by lazy {
        CallWithTmdbAccount(appScope = appScope, isTmdbLinked = isTmdbLinked)
    }

    @Test
    fun `does skip when isTmdbLinked is false and call returns right`() = appScope.runTest(
        dispatchTimeoutMs = TestTimeoutMs
    ) {
        // given
        every { isTmdbLinked() } returns flowOf(false)
        val expected = NetworkOperation.Skipped.left()

        // when
        val result = callWithTmdbAccount { 2.right() }

        // then
        assertEquals(expected, result)
    }

    @Test
    fun `does not skip when isTmdbLinked was false then true and call returns right`() = appScope.runTest(
        dispatchTimeoutMs = TestTimeoutMs
    ) {
        // given
        every { isTmdbLinked() } returns flowOf(false, true)
        val right = 2.right()

        // when
        val result = callWithTmdbAccount { right }

        // then
        assertEquals(right, result)
    }

    @Test
    fun `does skip when isTmdbLinked is false and call returns left`() = appScope.runTest(
        dispatchTimeoutMs = TestTimeoutMs
    ) {
        // given
        every { isTmdbLinked() } returns flowOf(false)
        val expected = NetworkOperation.Skipped.left()

        // when
        val result = callWithTmdbAccount { NetworkError.Unauthorized.left() }

        // then
        assertEquals(expected, result)
    }

    @Test
    fun `does not skip when isTmdbLinked was false then true and call returns left`() = appScope.runTest(
        dispatchTimeoutMs = TestTimeoutMs
    ) {
        // given
        every { isTmdbLinked() } returns flowOf(false, true)
        val error = NetworkError.Unauthorized
        val expected = NetworkOperation.Error(error).left()

        // when
        val result = callWithTmdbAccount { error.left() }

        // then
        assertEquals(expected, result)
    }

    @Test
    fun `does not skip when isTmdbLinked is true and call returns right`() = appScope.runTest(
        dispatchTimeoutMs = TestTimeoutMs
    ) {
        // given
        every { isTmdbLinked() } returns flowOf(true)
        val right = 2.right()

        // when
        val result = callWithTmdbAccount { right }

        // then
        assertEquals(right, result)
    }

    @Test
    fun `does skip when isTmdbLinked was true then false and call returns right`() = appScope.runTest(
        dispatchTimeoutMs = TestTimeoutMs
    ) {
        // given
        every { isTmdbLinked() } returns flowOf(true, false)
        val expected = NetworkOperation.Skipped.left()

        // when
        val result = callWithTmdbAccount { 2.right() }

        // then
        assertEquals(expected, result)
    }

    @Test
    fun `does not skip when isTmdbLinked is true and call returns left`() = appScope.runTest(
        dispatchTimeoutMs = TestTimeoutMs
    ) {
        // given
        every { isTmdbLinked() } returns flowOf(true)
        val error = NetworkError.Unauthorized
        val expected = NetworkOperation.Error(error).left()

        // when
        val result = callWithTmdbAccount { error.left() }

        // then
        assertEquals(expected, result)
    }

    @Test
    fun `does skip when isTmdbLinked was true then false and call returns left`() = appScope.runTest(
        dispatchTimeoutMs = TestTimeoutMs
    ) {
        // given
        every { isTmdbLinked() } returns flowOf(true, false)
        val expected = NetworkOperation.Skipped.left()

        // when
        val result = callWithTmdbAccount { NetworkError.Unauthorized.left() }

        // then
        assertEquals(expected, result)
    }
}
