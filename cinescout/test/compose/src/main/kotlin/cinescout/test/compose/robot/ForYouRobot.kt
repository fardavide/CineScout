package cinescout.test.compose.robot

import androidx.activity.ComponentActivity
import androidx.compose.runtime.Composable
import androidx.compose.ui.test.AndroidComposeUiTest

class ForYouRobot<T : ComponentActivity> internal constructor(
    composeTest: AndroidComposeUiTest<T>
) : HomeRobot<T>(composeTest) {

    fun verify(block: Verify<T>.() -> Unit): ForYouRobot<T> =
        also { Verify(composeTest).block() }

    class Verify<T : ComponentActivity>(composeTest: AndroidComposeUiTest<T>) : HomeRobot.Verify<T>(composeTest) {

    }
}

fun <T : ComponentActivity> AndroidComposeUiTest<T>.ForYouRobot(content: @Composable () -> Unit) =
    ForYouRobot(this).also { setContent(content) }
