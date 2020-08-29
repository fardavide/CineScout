package entities.util

import client.DispatchersProvider
import kotlinx.coroutines.test.TestCoroutineDispatcher

class TestDispatchersProvider : DispatchersProvider {
    override val Main = TestCoroutineDispatcher()
    override val Comp = Main
    override val Io = Main
}
