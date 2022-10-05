package cinescout.test.compose.robot

import androidx.activity.ComponentActivity
import androidx.compose.runtime.Composable
import androidx.compose.ui.test.AndroidComposeUiTest

class MovieDetailsRobot<T : ComponentActivity> internal constructor(
    private val composeTest: AndroidComposeUiTest<T>
) {

    fun awaitIdle(): MovieDetailsRobot<T> {
        composeTest.waitForIdle()
        return this
    }

    fun verify(block: Verify<T>.() -> Unit) = Verify(composeTest).apply(block)

    class Verify<T : ComponentActivity> internal constructor(
        private val composeTest: AndroidComposeUiTest<T>
    )
}

fun <T : ComponentActivity> AndroidComposeUiTest<T>.MovieDetailsRobot(content: @Composable () -> Unit) =
    MovieDetailsRobot(this).also { setContent(content) }
