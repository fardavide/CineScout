package cinescout.test.compose.robot

import androidx.activity.ComponentActivity
import androidx.compose.runtime.Composable
import androidx.compose.ui.test.AndroidComposeUiTest
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onRoot
import androidx.compose.ui.test.performTouchInput
import androidx.compose.ui.test.swipeRight
import cinescout.design.TestTag

open class HomeRobot<T : ComponentActivity>(protected val composeTest: AndroidComposeUiTest<T>) {

    fun openDrawer(): HomeDrawerRobot<T> {
        composeTest.onRoot().performTouchInput { swipeRight() }
        return HomeDrawerRobot(composeTest)
    }

    fun verify(block: Verify<T>.() -> Unit): HomeRobot<T> =
        also { Verify<T>(composeTest).block() }

    open class Verify<T : ComponentActivity> internal constructor(protected val composeTest: AndroidComposeUiTest<T>) {

        fun drawerIsClosed() {
            composeTest.onNodeWithTag(TestTag.Drawer)
            // TODO: how to verify it's closed?
        }

        fun drawerIsOpen() {
            composeTest.onNodeWithTag(TestTag.Drawer)
                .assertIsDisplayed()
        }

        fun forYouIsDisplayed() {
            composeTest.onNodeWithTag(TestTag.ForYou)
                .assertIsDisplayed()
        }
    }
}

fun <T : ComponentActivity> AndroidComposeUiTest<T>.HomeRobot(content: @Composable () -> Unit) =
    HomeRobot(this).also { setContent(content) }
