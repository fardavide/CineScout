package cinescout.android

import cinescout.design.Destination
import cinescout.details.presentation.ui.ScreenplayDetailsScreen

sealed class AppDestination(path: String, vararg keys: String) : Destination(path, *keys) {

    object Home : AppDestination("home")
    object ManageAccount : AppDestination("manage_account")
    object ScreenplayDetails : AppDestination("screenplay_details", ScreenplayDetailsScreen.ScreenplayIdKey)
}
