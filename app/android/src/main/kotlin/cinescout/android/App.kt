package cinescout.android

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import cinescout.account.presentation.ui.ManageAccountScreen
import cinescout.design.NavHost
import cinescout.design.composable
import cinescout.design.get
import cinescout.design.navigate
import cinescout.design.theme.CineScoutTheme
import cinescout.details.presentation.ui.ScreenplayDetailsScreen
import cinescout.home.presentation.ui.HomeScreen

@Composable
internal fun App(onFinish: () -> Unit) {
    val navController = rememberNavController()
    val onBack = { navController.popOrFinish(onFinish) }
    NavHost(navController = navController, startDestination = AppDestination.Home) {
        composable(AppDestination.ManageAccount) {
            ManageAccountScreen(back = onBack)
        }
        composable(AppDestination.Home) {
            val homeScreenActions = HomeScreen.Actions(
                toManageAccount = { navController.navigate(AppDestination.ManageAccount) },
                toScreenplayDetails = { screenplayId -> navController.navigate(AppDestination.ScreenplayDetails, screenplayId) }
            )
            HomeScreen(actions = homeScreenActions)
        }
        composable(AppDestination.ScreenplayDetails) { backStackEntry ->
            val screenplayDetailsActions = ScreenplayDetailsScreen.Actions(
                onBack = onBack
            )
            ScreenplayDetailsScreen(
                screenplayId = backStackEntry[ScreenplayDetailsScreen.ScreenplayIdKey],
                actions = screenplayDetailsActions
            )
        }
    }
}

private fun NavController.popOrFinish(onFinish: () -> Unit) {
    if (popBackStack().not()) onFinish()
}

@Composable
@Preview(showBackground = true)
private fun AppPreview() {
    CineScoutTheme {
        Surface(modifier = Modifier.fillMaxSize(), color = MaterialTheme.colorScheme.background) {
            App(onFinish = {})
        }
    }
}
