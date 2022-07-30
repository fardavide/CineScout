package cinescout.home.presentation.ui

import androidx.compose.material3.DrawerValue
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import cinescout.home.presentation.model.HomeState
import cinescout.test.compose.robot.HomeDrawerRobot
import cinescout.test.compose.runComposeTest
import kotlin.test.Test

class HomeDrawerTest {

    @Test
    fun loginIsNotSelectedOnClick() = runComposeTest {
        HomeDrawerRobot { HomeDrawer() }
            .selectLogin()
            .verify { loginIsNotSelected() }
    }

    @Test
    fun loginIsDisplayed() = runComposeTest {
        HomeDrawerRobot { HomeDrawer() }
            .verify { loginIsDisplayed() }
    }

    @Composable
    private fun HomeDrawer(
        accountState: HomeState.Account = HomeState.Account.Loading,
        drawerValue: DrawerValue = DrawerValue.Open
    ) {
        val drawerState = rememberDrawerState(initialValue = drawerValue)
        HomeDrawer(accountState = accountState, content = {}, drawerState = drawerState, onItemClick = {})
    }
}
