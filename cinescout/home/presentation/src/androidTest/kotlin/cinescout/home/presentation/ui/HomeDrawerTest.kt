package cinescout.home.presentation.ui

import androidx.compose.material3.DrawerValue
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.onNodeWithText
import cinescout.home.presentation.model.HomeState
import cinescout.home.presentation.testdata.HomeStateTestData
import cinescout.home.presentation.testdata.HomeStateTestData.buildHomeState
import cinescout.test.compose.robot.HomeDrawerRobot
import cinescout.test.compose.runComposeTest
import kotlin.test.Test

class HomeDrawerTest {

    @Test
    fun whenStart_forYouIsSelected() = runComposeTest {
        HomeDrawerRobot { HomeDrawer() }
            .verify { forYouIsSelected() }
    }

    @Test
    fun whenNotLoggedIn_loginIsDisplayed() = runComposeTest {
        HomeDrawerRobot { HomeDrawer() }
            .verify { accountsIsDisplayed() }
    }

    @Test
    fun whenLoggedInToTmdb_accountUsernameIsDisplayed() = runComposeTest {
        val account = HomeStateTestData.TmdbAccount
        val homeState = buildHomeState {
            accounts {
                tmdb = account
            }
        }
        HomeDrawerRobot { HomeDrawer(homeState) }
            .verify {
                onNodeWithText(account.username).assertIsDisplayed()
            }
    }

    @Test
    fun whenLoggedInToTrakt_accountUsernameIsDisplayed() = runComposeTest {
        val account = HomeStateTestData.TraktAccount
        val homeState = buildHomeState {
            accounts {
                trakt = account
            }
        }
        HomeDrawerRobot { HomeDrawer(homeState) }
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
        HomeDrawerRobot { HomeDrawer(homeState) }
            .verify { appVersionIsDisplayed(appVersion) }
    }

    @Test
    fun whenAccountClick_isNotSelected() = runComposeTest {
        HomeDrawerRobot { HomeDrawer() }
            .selectAccounts()
            .verify { accountsIsNotSelected() }
    }

    @Test
    fun whenForYouClick_isSelected() = runComposeTest {
        HomeDrawerRobot { HomeDrawer() }
            .selectForYou()
            .verify { forYouIsSelected() }
    }

    @Test
    fun whenWatchlistClick_isSelected() = runComposeTest {
        HomeDrawerRobot { HomeDrawer() }
            .selectWatchlist()
            .verify { watchlistIsSelected() }
    }

    @Composable
    private fun HomeDrawer(
        homeState: HomeState = HomeState.Loading,
        drawerValue: DrawerValue = DrawerValue.Open
    ) {
        val drawerState = rememberDrawerState(initialValue = drawerValue)
        HomeDrawer(homeState = homeState, content = {}, drawerState = drawerState, onItemClick = {})
    }
}
