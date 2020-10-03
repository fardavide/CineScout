package client.android.ui

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import client.Screen
import client.android.Get
import client.resource.Strings
import client.viewModel.WatchlistViewModel

@Composable
fun Watchlist(buildViewModel: Get<WatchlistViewModel>, toSearch: () -> Unit, toSuggestions: () -> Unit) {

    HomeScaffold(
        currentScreen = Screen.Watchlist,
        topBar = { TitleTopBar(title = Strings.WatchlistAction) },
        toSearch = toSearch,
        toSuggestions = toSuggestions,
        toWatchlist = {}
    ) {

        val scope = rememberCoroutineScope()
        val viewModel = remember {
            buildViewModel(scope)
        }

    }
}
