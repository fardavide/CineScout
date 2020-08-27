package entities.util

import client.DispatchersProvider
import client.ViewStateFlow
import client.ViewStatePublisher
import client.viewModel.CineViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.test.TestCoroutineScope

/**
 * Inherit from this class for tests that have to publish to [ViewStateFlow]
 */
abstract class ViewStateTest : ViewStatePublisher
