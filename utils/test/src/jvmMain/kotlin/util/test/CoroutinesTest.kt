package util.test

import kotlinx.coroutines.test.TestCoroutineScope
import kotlinx.coroutines.test.runBlockingTest
import util.DispatchersProvider
import kotlin.coroutines.CoroutineContext

/**
 * Implement this for tests that use structured concurrency
 * One should use [coroutinesTest] instead of [runBlockingTest]
 */
interface CoroutinesTest {

    val dispatchers: DispatchersProvider get() =
        TestDispatchersProvider

    /**
     * @see runBlockingTest with [DispatchersProvider.Main] from [dispatchers] as default [context]
     */
    fun coroutinesTest(
        context: CoroutineContext = dispatchers.Main,
        testBody: suspend TestCoroutineScope.() -> Unit
    ) = runBlockingTest(context, testBody)
}
