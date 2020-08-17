package client.cli.util

import client.DispatchersProvider
import kotlinx.coroutines.test.TestCoroutineDispatcher

class TestDispatchersProvider : DispatchersProvider {
    override val Main = TestCoroutineDispatcher()
    override val Comp = TestCoroutineDispatcher()
    override val Io = TestCoroutineDispatcher()
}
