package client.android.ui

import androidx.compose.animation.Crossfade
import androidx.compose.material.Icon
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.BottomAppBar
import androidx.compose.material.BottomDrawerLayout
import androidx.compose.material.BottomDrawerState
import androidx.compose.material.BottomDrawerValue
import androidx.compose.material.FabPosition
import androidx.compose.material.IconButton
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Scaffold
import androidx.compose.material.Surface
import androidx.compose.material.Text
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
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.unit.dp
import client.Navigator
import client.Screen
import client.android.getWithScope
import client.android.getWithScopeAndId
import client.android.theme.CineScoutTheme
import client.android.theme.default
import client.android.util.ThemedPreview
import client.data
import client.onlyData
import org.koin.core.Koin
import util.exhaustive

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

                Screen.Home -> Home(
                    koin = koin,
                    toSearch = navigator::toSearch,
                    toSuggestions = navigator::toSuggestions,
                    toWatchlist = navigator::toWatchlist
                )

                is Screen.MovieDetails -> MovieDetails(
                    buildViewModel = koin::getWithScopeAndId,
                    movieId = screen.movie.id,
                    onBack = navigator::back,
                )

                Screen.Search -> SearchMovie(
                    koin = koin,
                    buildViewModel = koin::getWithScope,
                    toSuggestions = navigator::toSuggestions,
                    toWatchlist = navigator::toWatchlist,
                    toMovieDetails = navigator::toMovieDetails,
                    logger = koin.get()
                )

                Screen.Suggestions -> Suggestions(
                    koin = koin,
                    buildViewModel = koin::getWithScope,
                    toMovieDetails = navigator::toMovieDetails,
                    toSearch = navigator::toSearch,
                    toWatchlist = navigator::toWatchlist,
                    logger = koin.get()
                )

                Screen.Watchlist -> Watchlist(
                    koin = koin,
                    buildViewModel = koin::getWithScope,
                    toSearch = navigator::toSearch,
                    toSuggestions = navigator::toSuggestions,
                    toMovieDetails = navigator::toMovieDetails,
                    logger = koin.get()
                )

            }.exhaustive
        }
    }
}

@Composable
fun HomeScaffold(
    koin: Koin,
    currentScreen: Screen,
    topBar: @Composable () -> Unit,
    floatingActionButton: @Composable () -> Unit = {},
    floatingActionButtonPosition: FabPosition = FabPosition.End,
    isFloatingActionButtonDocked: Boolean = false,
    toSearch: () -> Unit,
    toSuggestions: () -> Unit,
    toWatchlist: () -> Unit,
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

        BottomDrawerLayout(drawerState = drawerState, gesturesEnabled = drawerState.isClosed.not(), drawerContent = {
            DrawerContent(koin::getWithScope, koin.get()) {
                Column {

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
                    DrawerItem(
                        screen = Screen.Watchlist,
                        current = currentScreen,
                        action = { drawerState.close(toWatchlist) })
                }
            }
        }) {
            content()
        }
    }
}

@Composable
fun MainScaffold(
    topBar: @Composable () -> Unit = {},
    bottomBar: @Composable () -> Unit = { BottomBar() },
    floatingActionButton: @Composable () -> Unit = {},
    floatingActionButtonPosition: FabPosition = FabPosition.End,
    isFloatingActionButtonDocked: Boolean = false,
    autoWrap: Boolean = true,
    content: @Composable (PaddingValues) -> Unit,
) {

    Scaffold(
        topBar = topBar,
        bottomBar = bottomBar,
        floatingActionButton = floatingActionButton,
        floatingActionButtonPosition = floatingActionButtonPosition,
        isFloatingActionButtonDocked = isFloatingActionButtonDocked,
    ) { innerPadding ->

        if (autoWrap)
            Box(modifier = Modifier.fillMaxSize().padding(innerPadding)) {
                content(innerPadding)
            }
        else
            content(innerPadding)
    }
}

const val TitleTopBarTestTag = "TitleTopBar test tag"
@Composable
fun TitleTopBar(title: String, modifier: Modifier = Modifier) {
    TopBar(modifier.testTag(TitleTopBarTestTag)) {
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            Text(
                style = MaterialTheme.typography.h5,
                color = MaterialTheme.colors.primary,
                text = title,
            )
        }
    }
}

@Composable
fun TopBar(modifier: Modifier = Modifier, content: @Composable RowScope.() -> Unit) {
    TopAppBar(modifier, backgroundColor = MaterialTheme.colors.surface) {
        content()
    }
}

@Composable
private fun BottomNavigationBar(drawerState: BottomDrawerState) {
    if (drawerState.isClosed)
        BottomBar(mainIcon = Icons.default.Menu, onMainClick = drawerState::open)
}

@Composable
fun BottomBar(
    mainIcon: ImageVector? = null,
    onMainClick: () -> Unit = {},
    otherButtons: @Composable () -> Unit = {},
) {
    BottomAppBar(
        backgroundColor = MaterialTheme.colors.surface,
        cutoutShape = MaterialTheme.shapes.large
    ) {

        if (mainIcon != null)
            IconButton(onClick = onMainClick) { Icon(mainIcon) }

        Box(modifier = Modifier.fillMaxSize().padding(vertical = 12.dp), contentAlignment = Alignment.CenterEnd) {
            otherButtons()
        }
    }
}
