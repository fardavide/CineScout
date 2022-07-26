package cinescout.test.compose.robot

import androidx.activity.ComponentActivity
import androidx.compose.ui.test.AndroidComposeUiTest

class LoginRobot<T : ComponentActivity> internal constructor(private val composeTest: AndroidComposeUiTest<T>) {

    fun verify(block: Verify<T>.() -> Unit): LoginRobot<T> =
        also { Verify(composeTest).block() }

    class Verify<T : ComponentActivity>(private val composeTest: AndroidComposeUiTest<T>) {

        fun loginToTmdbIsDisplayed() {
            TODO("Not implemented")
        }

        fun loginToTraktIsDisplayed() {
            TODO("Not implemented")
        }
    }
}
