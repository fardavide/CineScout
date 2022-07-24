package cinescout.android.testutil

import androidx.compose.ui.test.AndroidComposeUiTest
import androidx.compose.ui.test.runAndroidComposeUiTest
import cinescout.android.MainActivity

fun runComposeAppTest(block: ComposeAppTest.() -> Unit) = runAndroidComposeUiTest(block)

typealias ComposeAppTest = AndroidComposeUiTest<MainActivity>
