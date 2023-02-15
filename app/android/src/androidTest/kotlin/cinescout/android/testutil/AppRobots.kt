package cinescout.android.testutil

import androidx.compose.ui.test.ComposeUiTest
import cinescout.test.compose.robot.ForYouRobot
import cinescout.test.compose.robot.HomeRobot
import cinescout.test.compose.semantic.HomeSemantics

context(ComposeUiTest)
val homeRobot get() = HomeSemantics { HomeRobot() }

val ComposeAppTest.forYouRobot: ForYouRobot get() = homeRobot.asForYou()
