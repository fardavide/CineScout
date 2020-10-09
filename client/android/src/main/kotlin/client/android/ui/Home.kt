package client.android.ui

import androidx.compose.runtime.Composable
import client.Screen
import client.resource.Strings

@Composable
fun Home(toSearch: () -> Unit, toSuggestions: () -> Unit, toWatchlist: () -> Unit) {

    HomeScaffold(
        currentScreen = Screen.Home,
        topBar = { TitleTopBar(title = Strings.AppName) },
        toSearch = toSearch,
        toSuggestions = toSuggestions,
        toWatchlist = toWatchlist,
        content = {

        }
    )
}
