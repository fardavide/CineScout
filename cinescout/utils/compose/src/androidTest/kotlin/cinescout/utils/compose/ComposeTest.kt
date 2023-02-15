package cinescout.utils.compose

import androidx.activity.ComponentActivity
import androidx.compose.ui.test.AndroidComposeUiTest
import androidx.compose.ui.test.runAndroidComposeUiTest

/**
 * Run Compose test for a Composable.
 */
internal fun runComposeTest(block: ComposeTest.() -> Unit) = runAndroidComposeUiTest(block = block)

internal fun <T : ComponentActivity> AndroidComposeUiTest<T>.onBackPressed() {
    runOnUiThread { requireActivity().onBackPressedDispatcher.onBackPressed() }
}
internal fun <T : ComponentActivity> AndroidComposeUiTest<T>.requireActivity() = requireNotNull(activity)

internal typealias ComposeTest = AndroidComposeUiTest<ComposeTestActivity>
