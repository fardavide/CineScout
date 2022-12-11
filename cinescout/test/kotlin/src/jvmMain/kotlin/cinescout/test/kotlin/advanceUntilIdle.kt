package cinescout.test.kotlin

import kotlinx.coroutines.test.TestScope
import kotlinx.coroutines.test.advanceUntilIdle

context(TestScope)
fun <T : Any> T.alsoAdvanceUntilIdle(): T {
    advanceUntilIdle()
    return this
}
