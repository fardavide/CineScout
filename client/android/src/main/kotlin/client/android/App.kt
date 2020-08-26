package client.android

import androidx.compose.animation.Crossfade
import androidx.compose.foundation.Box
import androidx.compose.foundation.Icon
import androidx.compose.foundation.Text
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.BottomAppBar
import androidx.compose.material.BottomDrawerLayout
import androidx.compose.material.BottomDrawerState
import androidx.compose.material.BottomDrawerValue
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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.ui.tooling.preview.Preview
import client.Screen
import client.android.theme.CineScoutTheme
import client.android.theme.default
import client.android.util.ThemedPreview
import client.data
import client.onlyData
import org.koin.core.Koin

@Composable
fun CineScoutApp(koin: Koin) {
    CineScoutTheme {
        AppContent(navigator = koin.get())
    }
}

@Composable
private fun AppContent(navigator: AndroidNavigator) {

    // TODO deal with different ViewState
    val screenState = navigator.screen
    val currentScreen by screenState.onlyData().collectAsState(initial = screenState.data!!)

    val drawerState = rememberBottomDrawerState(initialValue = BottomDrawerValue.Closed)

    Scaffold(
        topBar = { TopBar(currentScreen.title) },
        bottomBar = { BottomBar(drawerState) },
    ) {

        BottomDrawerLayout(drawerState = drawerState, drawerContent = {}) {
            Crossfade(current = navigator.screen.data) {
                Surface(color = MaterialTheme.colors.background) {
                    // TODO body of the app
                }
            }
        }
    }

}

@Composable
private fun TopBar(title: String) {
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
private fun BottomBar(drawerState: BottomDrawerState) {
    if (drawerState.isClosed) {
        BottomAppBar(backgroundColor = MaterialTheme.colors.surface) {
            IconButton(onClick = drawerState::open) {
                Icon(Icons.default.Menu)
            }
        }
    }
}

// TODO place in the right place and deal with translations
private val Screen.title get() = when (this) {
    Screen.Home -> "CineScout"
    Screen.Search -> "Search"
    Screen.Suggestions -> "Suggestions"
}

@Composable
@Preview("Home screen")
private fun LightAppContentPreview() {
    ThemedPreview {
        AppContent(AndroidNavigator())
    }
}

@Composable
@Preview("Home screen dark")
private fun DarkAppContentPreview() {
    ThemedPreview(darkTheme = true) {
        AppContent(AndroidNavigator())
    }
}
