package client.android.ui

import androidx.compose.animation.Crossfade
import androidx.compose.foundation.Box
import androidx.compose.foundation.Icon
import androidx.compose.foundation.Image
import androidx.compose.foundation.Text
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.BottomAppBar
import androidx.compose.material.BottomDrawerLayout
import androidx.compose.material.BottomDrawerState
import androidx.compose.material.BottomDrawerValue
import androidx.compose.material.FabPosition
import androidx.compose.material.IconButton
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Scaffold
import androidx.compose.material.Surface
import androidx.compose.material.TopAppBar
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Menu
import androidx.compose.material.rememberBottomDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import client.Navigator
import client.Screen
import client.android.getWithScope
import client.android.icon
import client.android.theme.CineScoutTheme
import client.android.theme.default
import client.android.title
import client.data
import client.onlyData
import org.koin.core.Koin

@Composable
fun CineScoutApp(koin: Koin) {
    CineScoutTheme {
        AppContent(koin = koin, navigator = koin.get())
    }
}

@Composable
private fun AppContent(koin: Koin, navigator: Navigator) {

    // TODO deal with different ViewState
    val screenViewStateFlow = navigator.screen
    val currentScreen by remember(screenViewStateFlow) {
        screenViewStateFlow.onlyData()
    }.collectAsState(initial = screenViewStateFlow.data!!)

    Crossfade(current = currentScreen) {
        Surface(color = MaterialTheme.colors.background) {

            @Suppress("UnnecessaryVariable") // Needed for smart cast
            when (val screen = currentScreen) {

                Screen.Home -> Home(toSearch = navigator::toSearch, toSuggestions = navigator::toSuggestions)

                is Screen.MovieDetails -> MovieDetails(
                    buildViewModel = koin::getWithScope,
                    movie = screen.movie,
                    onBack = navigator::back,
                )

                Screen.Search -> SearchMovie(
                    buildViewModel = koin::getWithScope,
                    query = "blow", // TODO real query
                    toSuggestions = navigator::toSuggestions,
                    toMovieDetails = navigator::toMovieDetails,
                    logger = koin.get()
                )

                Screen.Suggestions -> Suggestions(
                    buildViewModel = koin::getWithScope,
                    toSearch = navigator::toSearch,
                    logger = koin.get()
                )

            }
        }
    }
}

@Composable
fun HomeScaffold(
    currentScreen: Screen,
    topBar: @Composable () -> Unit,
    floatingActionButton: @Composable (() -> Unit)? = null,
    floatingActionButtonPosition: FabPosition = FabPosition.End,
    isFloatingActionButtonDocked: Boolean = false,
    toSearch: () -> Unit,
    toSuggestions: () -> Unit,
    content: @Composable () -> Unit
) {
    val drawerState = rememberBottomDrawerState(initialValue = BottomDrawerValue.Closed)

    MainScaffold(
        topBar = topBar,
        bottomBar = { BottomNavigationBar(drawerState) },
        floatingActionButton = floatingActionButton,
        floatingActionButtonPosition = floatingActionButtonPosition,
        isFloatingActionButtonDocked = isFloatingActionButtonDocked,
    ) {

        BottomDrawerLayout(drawerState = drawerState, drawerContent = {
            DrawerContent {
                DrawerItem(
                    screen = Screen.Search,
                    current = currentScreen,
                    action = { drawerState.close(toSearch) }
                )
                DrawerItem(
                    screen = Screen.Suggestions,
                    current = currentScreen,
                    action = { drawerState.close(toSuggestions) }
                )
            }
        }) {
            content()
        }
    }
}

@Composable
fun MainScaffold(
    topBar: @Composable() () -> Unit,
    bottomBar: @Composable() (() -> Unit)? = { BottomBar() },
    floatingActionButton: @Composable() (() -> Unit)? = null,
    floatingActionButtonPosition: FabPosition = FabPosition.End,
    isFloatingActionButtonDocked: Boolean = false,
    content: @Composable() () -> Unit
) {

    Scaffold(
        topBar = topBar,
        bottomBar = bottomBar,
        floatingActionButton = floatingActionButton,
        floatingActionButtonPosition = floatingActionButtonPosition,
        isFloatingActionButtonDocked = isFloatingActionButtonDocked,
    ) { innerPadding ->

        Box(modifier = Modifier.fillMaxSize().padding(innerPadding)) {
            content()
        }
    }
}

@Composable
fun TopBar(title: String) {
    TopAppBar(backgroundColor = MaterialTheme.colors.surface) {
        Box(modifier = Modifier.fillMaxSize(), gravity = Alignment.Center) {
            Text(
                style = MaterialTheme.typography.h5,
                color = MaterialTheme.colors.primary,
                text = title,
            )
        }
    }
}

@Composable
private fun BottomNavigationBar(drawerState: BottomDrawerState) {
    if (drawerState.isClosed) {
        BottomBar {
            IconButton(onClick = drawerState::open) {
                Icon(Icons.default.Menu)
            }
        }
    }
}

@Composable
fun BottomBar(content: @Composable () -> Unit = {}) {

    BottomAppBar(
        backgroundColor = MaterialTheme.colors.surface,
        cutoutShape = MaterialTheme.shapes.large
    ) {
        content()
    }

}

@Composable
private fun DrawerContent(content: @Composable () -> Unit) {
    Box(modifier = Modifier.padding(horizontal = 8.dp, vertical = 16.dp)) {
        content()
    }
}

@Composable
private fun DrawerItem(screen: Screen, current: Screen, action: () -> Unit) {

    @Composable
    fun Modifier.maybeBackground() =
        if (screen == current)
            background(
                color = MaterialTheme.colors.secondary.copy(alpha = 0.3f),
                shape = MaterialTheme.shapes.medium
            )
        else this

    Row(modifier = Modifier.padding(8.dp).fillMaxWidth()) {
        Row(Modifier
            .clickable(onClick = action)
            .maybeBackground()
            .padding(horizontal = 24.dp, vertical = 16.dp)
            .fillMaxWidth()
        ) {
            Image(
                modifier = Modifier.size(48.dp),
                asset = screen.icon,
            )
            Text(
                modifier = Modifier.padding(start = 42.dp).gravity(Alignment.CenterVertically),
                style = MaterialTheme.typography.h5,
                text = screen.title,
            )
        }
    }
}


// @Composable
// @Preview("Home screen")
// private fun LightAppContentPreview() {
//     ThemedPreview {
//         AppContent(AndroidNavigator())
//     }
// }
//
// @Composable
// @Preview("Home screen dark")
// private fun DarkAppContentPreview() {
//     ThemedPreview(darkTheme = true) {
//         AppContent(AndroidNavigator())
//     }
// }

