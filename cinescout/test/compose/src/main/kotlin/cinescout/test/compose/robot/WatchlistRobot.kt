package cinescout.test.compose.robot

import androidx.activity.ComponentActivity
import androidx.compose.runtime.Composable
import androidx.compose.ui.test.AndroidComposeUiTest

class WatchlistRobot<T : ComponentActivity> internal constructor(
    composeTest: AndroidComposeUiTest<T>
) : HomeRobot<T>(composeTest) {

    class Verify<T : ComponentActivity>(composeTest: AndroidComposeUiTest<T>) : HomeRobot.Verify<T>(composeTest) {

    }

    companion object {

        fun <T : ComponentActivity> WatchlistRobot<T>.verify(
            block: WatchlistRobot.Verify<T>.() -> Unit
        ): WatchlistRobot<T> =
            also { WatchlistRobot.Verify(composeTest).block() }
    }
}

fun <T : ComponentActivity> AndroidComposeUiTest<T>.WatchlistRobot(content: @Composable () -> Unit) =
    WatchlistRobot(this).also { setContent(content) }
