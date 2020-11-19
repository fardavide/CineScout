package client.android.ui

import androidx.compose.runtime.Composable
import client.Screen
import client.resource.Strings
import org.koin.core.Koin

@Composable
fun Home(koin: Koin, toSearch: () -> Unit, toSuggestions: () -> Unit, toWatchlist: () -> Unit) {

    HomeScaffold(
        koin,
        currentScreen = Screen.Home,
        topBar = { TitleTopBar(title = Strings.AppName) },
        toSearch = toSearch,
        toSuggestions = toSuggestions,
        toWatchlist = toWatchlist,
        content = {

        }
    )
}
