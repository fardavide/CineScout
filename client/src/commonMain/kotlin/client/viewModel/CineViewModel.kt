package client.viewModel

import client.DispatchersProvider
import client.ViewStatePublisher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.cancel

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
}
