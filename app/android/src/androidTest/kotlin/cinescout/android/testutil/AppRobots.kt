package cinescout.android.testutil

import cinescout.android.MainActivity
import cinescout.test.compose.robot.ForYouRobot
import cinescout.test.compose.robot.HomeRobot

val ComposeAppTest.homeRobot get() = HomeRobot(this)

val ComposeAppTest.forYouRobot: ForYouRobot<MainActivity> get() = homeRobot.asForYou()
