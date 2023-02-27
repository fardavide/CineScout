package cinescout.android

import cinescout.android.testutil.PostNotificationsRule
import cinescout.android.testutil.homeRobot
import cinescout.android.testutil.runComposeAppTest
import cinescout.lists.presentation.ui.ListTypeSelector
import cinescout.suggestions.presentation.ui.ForYouTypeSelector
import cinescout.test.mock.junit4.MockAppRule
import org.junit.Rule
import kotlin.test.BeforeTest
import kotlin.test.Test

class HomeFlowTest {

    @get:Rule
    val appRule = MockAppRule {
        newInstall()
    }

    @get:Rule
    val permissionsRule = PostNotificationsRule()

    @BeforeTest
    fun setup() {
        ListTypeSelector.animateChanges = false
        ForYouTypeSelector.animateChanges = false
    }

    @Test
    fun whenForYouIsClicked_screenIsDisplayed() = runComposeAppTest {
        homeRobot
            .openForYou()
            .verify { screenIsDisplayed() }
    }

    @Test
    fun whenWatchlistIsClicked_screenIsDisplayed() = runComposeAppTest {
        homeRobot
            .openWatchlist()
            .verify { watchlistIsDisplayed() }
    }

    @Test
    fun whenMyListsIsClicked_screenIsDisplayed() = runComposeAppTest {
        homeRobot
            .openMyLists()
            .verify { myListsIsDisplayed() }
    }

    @Test
    fun whenProfileIsClicked_manageAccountIsDisplayed() = runComposeAppTest {
        homeRobot
            .openProfile()
            .verify {
                connectButtonsAreDisplayed()
            }
    }
}
