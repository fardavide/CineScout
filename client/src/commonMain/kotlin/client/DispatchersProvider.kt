package client

import kotlinx.coroutines.CoroutineDispatcher

/**
 * Provides [CoroutineDispatcher] that will run on different thread pools
 *
 * @property Main executes on the main (single) thread
 * @property Comp executes on a thread pool dedicated to computation intensive operations
 * @property Io executes on a thread pool dedicated to I/O operations
 */
interface DispatchersProvider {

    val Main: CoroutineDispatcher
    val Comp: CoroutineDispatcher
    val Io: CoroutineDispatcher
}
