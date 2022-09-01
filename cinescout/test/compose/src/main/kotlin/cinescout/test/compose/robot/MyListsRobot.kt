package cinescout.test.compose.robot

import androidx.activity.ComponentActivity
import androidx.compose.runtime.Composable
import androidx.compose.ui.test.AndroidComposeUiTest

class MyListsRobot<T : ComponentActivity> internal constructor(
    composeTest: AndroidComposeUiTest<T>
) : HomeRobot<T>(composeTest) {

    class Verify<T : ComponentActivity>(composeTest: AndroidComposeUiTest<T>) : HomeRobot.Verify<T>(composeTest) {

    }

    companion object {

        fun <T : ComponentActivity> MyListsRobot<T>.verify(
            block: MyListsRobot.Verify<T>.() -> Unit
        ): MyListsRobot<T> =
            also { MyListsRobot.Verify(composeTest).block() }
    }
}

fun <T : ComponentActivity> AndroidComposeUiTest<T>.MyListsRobot(content: @Composable () -> Unit) =
    MyListsRobot(this).also { setContent(content) }
