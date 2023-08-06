package cinescout.android

import cinescout.design.Destination
import cinescout.details.presentation.ui.ScreenplayDetailsScreen

sealed class AppDestination(path: String, vararg keys: String) : Destination(path, *keys) {

    data object Home : AppDestination("home")
    data object ManageAccount : AppDestination("manage_account")
    data object Report : AppDestination("report")
    data object ScreenplayDetails : AppDestination("screenplay_details", ScreenplayDetailsScreen.ScreenplayIdsKey)
    data object Settings : AppDestination("settings")
}
