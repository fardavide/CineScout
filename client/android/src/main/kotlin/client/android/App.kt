package client.android

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
import androidx.compose.ui.graphics.vector.VectorAsset
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.unit.dp
import androidx.ui.tooling.preview.Preview
import client.Screen
import client.android.theme.CineScoutTheme
import client.android.theme.default
import client.android.util.ThemedPreview
import client.data
import client.onlyData
import entities.util.plus
import org.koin.core.Koin
import studio.forface.cinescout.R

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

        BottomDrawerLayout(drawerState = drawerState, drawerContent = {
            DrawerContent {
                DrawerItem(
                    screen = Screen.Search,
                    current = currentScreen,
                    action = navigator::toSearch + drawerState::close
                )
                DrawerItem(
                    screen = Screen.Suggestions,
                    current = currentScreen,
                    action = navigator::toSuggestions + drawerState::close
                )
            }
        }) {
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
        BottomAppBar(
            backgroundColor = MaterialTheme.colors.surface,
            cutoutShape = MaterialTheme.shapes.large
        ) {
            IconButton(onClick = drawerState::open) {
                Icon(Icons.default.Menu)
            }
        }
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

@Composable
private val Screen.title: String get() {
    val id = when (this) {
        Screen.Home -> R.string.application_name
        Screen.Search -> R.string.action_search
        Screen.Suggestions -> R.string.action_suggestions
    }
    return stringResource(id)
}

@Composable
private val Screen.icon: VectorAsset get() {
    val id = when (this) {
        Screen.Home -> R.drawable.ic_3d_glasses_color
        Screen.Search -> R.drawable.ic_search_color
        Screen.Suggestions -> R.drawable.ic_diamond_color
    }
    return vectorResource(id)
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
