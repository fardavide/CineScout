package client.util

import client.ViewStatePublisher
import client.viewModel.CineViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.test.TestCoroutineScope
import kotlinx.coroutines.test.runBlockingTest
import util.test.CoroutinesTest
import kotlin.coroutines.CoroutineContext

/**
 * Implement this for test ViewModels
 * Implements [ViewStateTest]
 * Implements indirectly [CoroutinesTest] and [ViewStatePublisher]
 *
 * One should use [viewModelTest] instead of [coroutinesTest] and [runBlockingTest]
 */
interface ViewModelTest : ViewStateTest {

    fun <VM : CineViewModel> viewModelTest(
        buildViewModel: suspend CoroutineScope.() -> VM,
        context: CoroutineContext = dispatchers.Main,
        testBody: suspend TestCoroutineScope.(VM) -> Unit
    ) = coroutinesTest(context) {

        buildViewModel(this).run {
            testBody(this)
            closeChannels()
        }
    }
}
