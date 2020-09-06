package util.test

import kotlinx.coroutines.test.TestCoroutineDispatcher
import util.DispatchersProvider

/**
 * Test purpose implementation of [DispatchersProvider]
 */
val TestDispatchersProvider = object : DispatchersProvider {

    override val Main = TestCoroutineDispatcher()
    override val Comp = Main
    override val Io = Main
}
