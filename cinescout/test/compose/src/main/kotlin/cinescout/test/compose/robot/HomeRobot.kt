package cinescout.test.compose.robot

import androidx.activity.ComponentActivity
import androidx.compose.runtime.Composable
import androidx.compose.ui.test.AndroidComposeUiTest
import androidx.compose.ui.test.onRoot
import androidx.compose.ui.test.performTouchInput
import androidx.compose.ui.test.swipeRight

class HomeRobot<T : ComponentActivity>(private val composeTest: AndroidComposeUiTest<T>) {

    fun openDrawer(): HomeDrawerRobot<T> {
        composeTest.onRoot().performTouchInput { swipeRight() }
        return HomeDrawerRobot(composeTest)
    }

    fun verify(block: Verify<T>.() -> Unit): HomeRobot<T> =
        also { Verify<T>(composeTest).block() }

    class Verify<T : ComponentActivity> internal constructor(private val composeTest: AndroidComposeUiTest<T>)
}

fun <T : ComponentActivity> AndroidComposeUiTest<T>.HomeRobot(content: @Composable () -> Unit) =
    HomeRobot(this).also { setContent(content) }
