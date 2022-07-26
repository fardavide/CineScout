package cinescout.android.testutil

import androidx.compose.ui.test.AndroidComposeUiTest
import androidx.compose.ui.test.runAndroidComposeUiTest
import cinescout.android.MainActivity
import cinescout.test.compose.robot.HomeRobot

val ComposeAppTest.homeRobot get() = HomeRobot(this)

fun runComposeAppTest(block: ComposeAppTest.() -> Unit) = runAndroidComposeUiTest(block)

typealias ComposeAppTest = AndroidComposeUiTest<MainActivity>
