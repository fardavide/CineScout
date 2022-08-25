package cinescout.test.compose.robot

import androidx.activity.ComponentActivity
import androidx.compose.runtime.Composable
import androidx.compose.ui.test.AndroidComposeUiTest
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.onNodeWithText

class ForYouRobot<T : ComponentActivity> internal constructor(
    composeTest: AndroidComposeUiTest<T>
) : HomeRobot<T>(composeTest) {

    class Verify<T : ComponentActivity>(composeTest: AndroidComposeUiTest<T>) : HomeRobot.Verify<T>(composeTest) {

        fun movieIsDisplayed(movieTitle: String) {
            composeTest.onNodeWithText(movieTitle)
                .assertIsDisplayed()
        }
    }

    companion object {

        fun <T : ComponentActivity> ForYouRobot<T>.verify(block: ForYouRobot.Verify<T>.() -> Unit): ForYouRobot<T> =
            also { ForYouRobot.Verify(composeTest).block() }
    }
}

fun <T : ComponentActivity> AndroidComposeUiTest<T>.ForYouRobot(content: @Composable () -> Unit) =
    ForYouRobot(this).also { setContent(content) }
