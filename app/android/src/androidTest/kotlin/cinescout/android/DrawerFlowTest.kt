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

class DrawerFlowTest {

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
    fun givenDrawerIsOpen_whenAccountsIsClicked_loginIsShown() = runComposeAppTest {
        homeRobot
            .openDrawer()
            .openAccounts()
            .verify {
                connectButtonsAreDisplayed()
            }
    }

    @Test
    fun givenDrawerIsOpen_whenForYouIsClicked_drawerIsClosed() = runComposeAppTest {
        homeRobot
            .openDrawer()
            .openForYou()
            .verify { drawerIsClosed() }
    }

    @Test
    fun givenDrawerIsOpen_whenForYouIsClicked_screenIsDisplayed() = runComposeAppTest {
        homeRobot
            .openDrawer()
            .openForYou()
            .verify { screenIsDisplayed() }
    }

    @Test
    fun givenDrawerIsOpen_whenWatchlistIsClicked_drawerIsClosed() = runComposeAppTest {
        homeRobot
            .openDrawer()
            .openWatchlist()
            .verify { drawerIsClosed() }
    }

    @Test
    fun givenDrawerIsOpen_whenWatchlistIsClicked_screenIsDisplayed() = runComposeAppTest {
        homeRobot
            .openDrawer()
            .openWatchlist()
            .verify { watchlistIsDisplayed() }
    }

    @Test
    fun givenDrawerIsOpen_whenMyListsIsClicked_drawerIsClosed() = runComposeAppTest {
        homeRobot
            .openDrawer()
            .openMyLists()
            .verify { drawerIsClosed() }
    }

    @Test
    fun givenDrawerIsOpen_whenMyListsIsClicked_screenIsDisplayed() = runComposeAppTest {
        homeRobot
            .openDrawer()
            .openMyLists()
            .verify { myListsIsDisplayed() }
    }
}
