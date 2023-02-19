package cinescout.home.presentation.ui

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.onNodeWithText
import cinescout.design.TestTag
import cinescout.design.ui.DrawerScaffold
import cinescout.home.presentation.model.HomeState
import cinescout.home.presentation.sample.HomeStateSample
import cinescout.home.presentation.sample.HomeStateSample.buildHomeState
import cinescout.test.compose.robot.HomeRobot
import cinescout.test.compose.runComposeTest
import kotlin.test.Test

class HomeDrawerTest {

    @Test
    fun whenStart_forYouIsSelected() = runComposeTest {
        HomeRobot { HomeDrawerScaffold() }
            .openDrawer()
            .verify { forYouIsSelected() }
    }

    @Test
    fun whenNotLoggedIn_loginIsDisplayed() = runComposeTest {
        HomeRobot { HomeDrawerScaffold() }
            .openDrawer()
            .verify { accountsIsDisplayed() }
    }

    @Test
    fun whenLoggedInToTmdb_accountUsernameIsDisplayed() = runComposeTest {
        val account = HomeStateSample.TmdbAccount
        val homeState = buildHomeState {
            accounts {
                tmdb = account
            }
        }
        HomeRobot { HomeDrawerScaffold(homeState) }
            .openDrawer()
            .verify {
                onNodeWithText(account.username).assertIsDisplayed()
            }
    }

    @Test
    fun whenLoggedInToTrakt_accountUsernameIsDisplayed() = runComposeTest {
        val account = HomeStateSample.TraktAccount
        val homeState = buildHomeState {
            accounts {
                trakt = account
            }
        }
        HomeRobot { HomeDrawerScaffold(homeState) }
            .openDrawer()
            .verify {
                onNodeWithText(account.username).assertIsDisplayed()
            }
    }

    @Test
    fun appVersionIsDisplayed() = runComposeTest {
        val appVersion = 123
        val homeState = buildHomeState {
            appVersionInt = appVersion
        }
        HomeRobot { HomeDrawerScaffold(homeState) }
            .openDrawer()
            .verify { appVersionIsDisplayed(appVersion) }
    }

    @Test
    fun whenAccountClick_isNotSelected() = runComposeTest {
        HomeRobot { HomeDrawerScaffold() }
            .openDrawer()
            .selectAccounts()
            .verify { accountsIsNotSelected() }
    }

    @Test
    fun whenForYouClick_isSelected() = runComposeTest {
        HomeRobot { HomeDrawerScaffold() }
            .openDrawer()
            .selectForYou()
            .verify { forYouIsSelected() }
    }

    @Test
    fun whenWatchlistClick_isSelected() = runComposeTest {
        HomeRobot { HomeDrawerScaffold() }
            .openDrawer()
            .selectWatchlist()
            .verify { watchlistIsSelected() }
    }

    @Composable
    private fun HomeDrawerScaffold(homeState: HomeState = HomeState.Loading) {
        DrawerScaffold(
            drawerContent = { HomeDrawerContent(homeState = homeState, onItemClick = {}) },
            content = { Text(modifier = Modifier.testTag(TestTag.BottomBar).fillMaxSize(), text = "content") }
        )
    }
}
