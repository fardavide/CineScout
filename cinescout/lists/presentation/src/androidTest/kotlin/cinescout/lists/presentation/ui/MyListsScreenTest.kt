package cinescout.lists.presentation.ui

import cinescout.test.compose.robot.MyListsRobot
import cinescout.test.compose.runComposeTest
import kotlin.test.Test

class MyListsScreenTest {

    @Test
    fun whenScreenIsDisplayed_ratedIsDisplayed() = runComposeTest {
        MyListsRobot { MyListsScreen(MyListsScreen.Actions.Empty) }
            .verify { ratedButtonIsDisplayed() }
    }

    @Test
    fun whenScreenIsDisplayed_likedIsDisplayed() = runComposeTest {
        MyListsRobot { MyListsScreen(MyListsScreen.Actions.Empty) }
            .verify { likedButtonIsDisplayed() }
    }

    @Test
    fun whenScreenIsDisplayed_dislikedIsDisplayed() = runComposeTest {
        MyListsRobot { MyListsScreen(MyListsScreen.Actions.Empty) }
            .verify { dislikedButtonIsDisplayed() }
    }
}
