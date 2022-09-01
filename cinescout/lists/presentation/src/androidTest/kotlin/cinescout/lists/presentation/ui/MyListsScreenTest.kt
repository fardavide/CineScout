package cinescout.lists.presentation.ui

import cinescout.test.compose.robot.MyListsRobot
import cinescout.test.compose.robot.MyListsRobot.Companion.verify
import cinescout.test.compose.runComposeTest
import kotlin.test.Test

class MyListsScreenTest {

    @Test
    fun whenScreenIsDisplayed_ratedIsDisplayed() = runComposeTest {
        MyListsRobot { MyListsScreen() }
            .verify { ratedIsDisplayed() }
    }

    @Test
    fun whenScreenIsDisplayed_likedIsDisplayed() = runComposeTest {
        MyListsRobot { MyListsScreen() }
            .verify { likedIsDisplayed() }
    }

    @Test
    fun whenScreenIsDisplayed_dislikedIsDisplayed() = runComposeTest {
        MyListsRobot { MyListsScreen() }
            .verify { dislikedIsDisplayed() }
    }
}
