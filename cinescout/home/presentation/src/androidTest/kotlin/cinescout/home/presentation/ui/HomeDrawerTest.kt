package cinescout.home.presentation.ui

import androidx.compose.material3.DrawerValue
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
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

    @Composable
    private fun HomeDrawer(drawerValue: DrawerValue = DrawerValue.Open) {
        val drawerState = rememberDrawerState(initialValue = drawerValue)
        HomeDrawer(content = {}, drawerState = drawerState, onItemClick = {})
    }
}
