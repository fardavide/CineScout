package cinescout.android

import cinescout.android.testutil.PostNotificationsRule
import cinescout.android.testutil.homeRobot
import cinescout.android.testutil.runComposeAppTest
import cinescout.test.compose.robot.HomeRobot.Companion.verify
import org.junit.Rule
import kotlin.test.Test

class NavigationFlowTest {

    @get:Rule
    val permissionsRule = PostNotificationsRule()

    @Test
    fun givenHomeIsDisplayed_whenForYouIsSelected_screenIsDisplayed() = runComposeAppTest {
        homeRobot
            .openDrawer()
            .selectForYou()
            .verify { forYouIsDisplayed() }
    }

    @Test
    fun givenHomeIsDisplayed_whenForYouIsOpen_subtitleIsDisplayed() = runComposeAppTest {
        homeRobot
            .openDrawer()
            .selectForYou()
            .verify { forYouSubtitleIsDisplayed() }
    }

    @Test
    fun givenHomeIsDisplayed_whenMyListsIsSelected_screenIsDisplayed() = runComposeAppTest {
        homeRobot
            .openDrawer()
            .selectMyLists()
            .verify { myListsIsDisplayed() }
    }

    @Test
    fun givenHomeIsDisplayed_whenMyListsIsOpen_subtitleIsDisplayed() = runComposeAppTest {
        homeRobot
            .openDrawer()
            .selectMyLists()
            .verify { myListSubtitleIsDisplayed() }
    }

    @Test
    fun givenHomeIsDisplayed_whenWatchlistIsSelected_screenIsDisplayed() = runComposeAppTest {
        homeRobot
            .openDrawer()
            .selectWatchlist()
            .verify { watchlistIsDisplayed() }
    }

    @Test
    fun givenHomeIsDisplayed_whenWatchlistIsOpen_subtitleIsDisplayed() = runComposeAppTest {
        homeRobot
            .openDrawer()
            .selectWatchlist()
            .verify { watchlistSubtitleIsDisplayed() }
    }

    @Test
    fun givenMyListsIsDisplayed_whenDislikedIsSelected_screenIsDisplayed() = runComposeAppTest {
        homeRobot
            .openDrawer()
            .openMyLists()
            .selectDisliked()
            .verify { dislikedIsDisplayed() }
    }

    @Test
    fun givenMyListsIsDisplayed_whenDislikedIsOpen_subtitleIsDisplayed() = runComposeAppTest {
        homeRobot
            .openDrawer()
            .openMyLists()
            .selectDisliked()
            .verify { dislikedSubtitleIsDisplayed() }
    }

    @Test
    fun givenMyListsIsDisplayed_whenLikedIsSelected_screenIsDisplayed() = runComposeAppTest {
        homeRobot
            .openDrawer()
            .openMyLists()
            .selectLiked()
            .verify { likedIsDisplayed() }
    }

    @Test
    fun givenMyListsIsDisplayed_whenLikedIsOpen_subtitleIsDisplayed() = runComposeAppTest {
        homeRobot
            .openDrawer()
            .openMyLists()
            .selectLiked()
            .verify { likedSubtitleIsDisplayed() }
    }

    @Test
    fun givenMyListsIsDisplayed_whenRatedIsSelected_screenIsDisplayed() = runComposeAppTest {
        homeRobot
            .openDrawer()
            .openMyLists()
            .selectRated()
            .verify { ratedIsDisplayed() }
    }

    @Test
    fun givenMyListsIsDisplayed_whenRatedIsOpen_subtitleIsDisplayed() = runComposeAppTest {
        homeRobot
            .openDrawer()
            .openMyLists()
            .selectRated()
            .verify { ratedSubtitleIsDisplayed() }
    }
}
