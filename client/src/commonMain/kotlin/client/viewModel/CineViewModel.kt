package client.viewModel

import client.ViewStatePublisher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.cancel
import util.DispatchersProvider
import kotlin.time.seconds

/**
 * Base ViewModel for clients
 * Implements [DispatchersProvider] for avoid to use hard-coded Dispatchers
 */
interface CineViewModel : ViewStatePublisher, DispatchersProvider {
    val scope: CoroutineScope

    /**
     * This function should close all the pending Channel's
     */
    fun closeChannels() {

    }

    fun onClear() {
        closeChannels()
        scope.cancel()
    }

    companion object {

        /**
         * Default delay for error.
         * It usually represents for how long an error must be shown or how long before to retry
         */
        val ErrorDelay = 3.seconds
    }
}
