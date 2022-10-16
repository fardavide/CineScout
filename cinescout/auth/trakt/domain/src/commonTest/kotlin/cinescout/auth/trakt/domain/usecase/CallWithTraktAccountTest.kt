package cinescout.auth.trakt.domain.usecase

import arrow.core.left
import arrow.core.right
import cinescout.error.NetworkError
import cinescout.model.NetworkOperation
import cinescout.test.kotlin.TestTimeout
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.TestScope
import kotlinx.coroutines.test.runTest
import kotlin.test.Test
import kotlin.test.assertEquals

internal class CallWithTraktAccountTest {

    private val appScope = TestScope()
    private val isTraktLinked: IsTraktLinked = mockk()
    private val callWithTraktAccount by lazy {
        CallWithTraktAccount(appScope = appScope, isTraktLinked = isTraktLinked)
    }

    @Test
    fun `does skip when isTraktLinked is false and call returns right`() = appScope.runTest(
        dispatchTimeoutMs = TestTimeout
    ) {
        // given
        every { isTraktLinked() } returns flowOf(false)
        val expected = NetworkOperation.Skipped.left()

        // when
        val result = callWithTraktAccount { 2.right() }

        // then
        assertEquals(expected, result)
    }

    @Test
    fun `does not skip when isTraktLinked was false then true and call returns right`() = appScope.runTest(
        dispatchTimeoutMs = TestTimeout
    ) {
        // given
        every { isTraktLinked() } returns flowOf(false, true)
        val right = 2.right()

        // when
        val result = callWithTraktAccount { right }

        // then
        assertEquals(right, result)
    }

    @Test
    fun `does skip when isTraktLinked is false and call returns left`() = appScope.runTest(
        dispatchTimeoutMs = TestTimeout
    ) {
        // given
        every { isTraktLinked() } returns flowOf(false)
        val expected = NetworkOperation.Skipped.left()

        // when
        val result = callWithTraktAccount { NetworkError.Unauthorized.left() }

        // then
        assertEquals(expected, result)
    }

    @Test
    fun `does not skip when isTraktLinked was false then true and call returns left`() = appScope.runTest(
        dispatchTimeoutMs = TestTimeout
    ) {
        // given
        every { isTraktLinked() } returns flowOf(false, true)
        val error = NetworkError.Unauthorized
        val expected = NetworkOperation.Error(error).left()

        // when
        val result = callWithTraktAccount { error.left() }

        // then
        assertEquals(expected, result)
    }

    @Test
    fun `does not skip when isTraktLinked is true and call returns right`() = appScope.runTest(
        dispatchTimeoutMs = TestTimeout
    ) {
        // given
        every { isTraktLinked() } returns flowOf(true)
        val right = 2.right()

        // when
        val result = callWithTraktAccount { right }

        // then
        assertEquals(right, result)
    }

    @Test
    fun `does skip when isTraktLinked was true then false and call returns right`() = appScope.runTest(
        dispatchTimeoutMs = TestTimeout
    ) {
        // given
        every { isTraktLinked() } returns flowOf(true, false)
        val expected = NetworkOperation.Skipped.left()

        // when
        val result = callWithTraktAccount { 2.right() }

        // then
        assertEquals(expected, result)
    }

    @Test
    fun `does not skip when isTraktLinked is true and call returns left`() = appScope.runTest(
        dispatchTimeoutMs = TestTimeout
    ) {
        // given
        every { isTraktLinked() } returns flowOf(true)
        val error = NetworkError.Unauthorized
        val expected = NetworkOperation.Error(error).left()

        // when
        val result = callWithTraktAccount { error.left() }

        // then
        assertEquals(expected, result)
    }

    @Test
    fun `does skip when isTraktLinked was true then false and call returns left`() = appScope.runTest(
        dispatchTimeoutMs = TestTimeout
    ) {
        // given
        every { isTraktLinked() } returns flowOf(true, false)
        val expected = NetworkOperation.Skipped.left()

        // when
        val result = callWithTraktAccount { NetworkError.Unauthorized.left() }

        // then
        assertEquals(expected, result)
    }
}
