package cinescout.test.compose.robot

import androidx.activity.ComponentActivity
import androidx.compose.runtime.Composable
import androidx.compose.ui.test.AndroidComposeUiTest
import androidx.compose.ui.test.assertIsNotSelected
import androidx.compose.ui.test.performClick
import cinescout.test.compose.util.onNodeWithText
import studio.forface.cinescout.design.R.string

class HomeDrawerRobot<T : ComponentActivity> internal constructor(private val composeTest: AndroidComposeUiTest<T>) {

    fun openLogin(): LoginRobot<T> {
        composeTest.onNodeWithText(string.home_login).performClick()
        return LoginRobot(composeTest)
    }

    fun selectLogin(): HomeDrawerRobot<T> {
        composeTest.onNodeWithText(string.home_login).performClick()
        return this
    }

    fun verify(block: Verify<T>.() -> Unit): HomeDrawerRobot<T> =
        also { Verify<T>(composeTest).block() }

    class Verify<T : ComponentActivity> internal constructor(private val composeTest: AndroidComposeUiTest<T>) {

        fun loginIsNotSelected() {
            composeTest.onNodeWithText(string.home_login)
                .assertIsNotSelected()
        }
    }
}

fun <T : ComponentActivity> AndroidComposeUiTest<T>.HomeDrawerRobot(content: @Composable () -> Unit) =
    HomeDrawerRobot(this).also { setContent(content) }
