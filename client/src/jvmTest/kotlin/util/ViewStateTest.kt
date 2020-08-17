package util

import client.DispatchersProvider
import client.ViewStateFlow
import client.viewModel.CineViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.test.TestCoroutineScope

/**
 * Inherit from this class for tests that have to publish to [ViewStateFlow]
 */
abstract class ViewStateTest : CineViewModel, DispatchersProvider by TestDispatchersProvider() {

    override val scope: CoroutineScope
        get() = TestCoroutineScope()
}
