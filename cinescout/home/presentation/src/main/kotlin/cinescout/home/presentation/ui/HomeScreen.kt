package cinescout.home.presentation.ui

import androidx.annotation.StringRes
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.AccountCircle
import androidx.compose.material.icons.rounded.Home
import androidx.compose.material.icons.rounded.Menu
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.Divider
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.NavigationDrawerItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import cinescout.design.theme.CineScoutTheme
import cinescout.design.theme.Dimens
import cinescout.design.util.NoContentDescription
import kotlinx.coroutines.launch
import studio.forface.cinescout.design.R.string

@Composable
fun HomeScreen(modifier: Modifier = Modifier) {
    val scope = rememberCoroutineScope()
    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)

    ModalNavigationDrawer(drawerState = drawerState, drawerContent = { HomeDrawerContent() }) {
        Scaffold(
            modifier = modifier
                .statusBarsPadding()
                .navigationBarsPadding(),
            bottomBar = { HomeBottomBar(openDrawer = { scope.launch { drawerState.open() } }) },
            topBar = { HomeTopBar() }
        ) { paddingValues ->
            Box(
                modifier = Modifier
                    .testTag(HomeScreen.TestTag)
                    .padding(paddingValues)
                    .fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                Text(text = stringResource(id = string.coming_soon), style = MaterialTheme.typography.displaySmall)
            }
        }
    }
}

// TODO extract to file
// TODO add tests
@Composable
private fun HomeDrawerContent() {
    var selectedItemIndex by remember { mutableStateOf(0) }
    Column(
        modifier = Modifier
            .statusBarsPadding()
            .navigationBarsPadding()
    ) {
        HomeDrawerItem.Standard(icon = Icons.Rounded.AccountCircle, label = string.home_login, onClick = { /* TODO */ })
        HomeDrawerDivider()
        HomeDrawerItem.Selectable(
            icon = Icons.Rounded.Home,
            label = string.coming_soon,
            selected = selectedItemIndex == 0,
            onClick = { selectedItemIndex = 0 }
        )
        HomeDrawerItem.Selectable(
            icon = Icons.Rounded.Home,
            label = string.coming_soon,
            selected = selectedItemIndex == 1,
            onClick = { selectedItemIndex = 1 }
        )
        HomeDrawerItem.Selectable(
            icon = Icons.Rounded.Home,
            label = string.coming_soon,
            selected = selectedItemIndex == 2,
            onClick = { selectedItemIndex = 2 }
        )
        HomeDrawerItem.Selectable(
            icon = Icons.Rounded.Home,
            label = string.coming_soon,
            selected = selectedItemIndex == 3,
            onClick = { selectedItemIndex = 3 }
        )
    }
}

@Composable
private fun HomeDrawerDivider() {
    Divider(
        modifier = Modifier.padding(vertical = Dimens.Margin.XSmall),
        color = MaterialTheme.colorScheme.primaryContainer
    )
}

object HomeDrawerItem {

    @Composable
    fun Standard(icon: ImageVector, @StringRes label: Int, onClick: () -> Unit) {
        Selectable(icon = icon, label = label, selected = false, onClick = onClick)
    }

    @Composable
    fun Selectable(icon: ImageVector, @StringRes label: Int, selected: Boolean, onClick: () -> Unit) {
        NavigationDrawerItem(
            label = {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        modifier = Modifier.size(Dimens.Icon.Medium),
                        imageVector = icon,
                        contentDescription = NoContentDescription
                    )
                    Spacer(modifier = Modifier.size(Dimens.Margin.Small))
                    Text(text = stringResource(id = label), style = MaterialTheme.typography.titleMedium)
                }
            },
            selected = selected,
            onClick = onClick
        )
    }
}

@Composable
private fun HomeTopBar() {
    CenterAlignedTopAppBar(
        title = { Text(text = stringResource(id = string.app_name)) }
    )
}

@Composable
private fun HomeBottomBar(openDrawer: () -> Unit) {
    BottomAppBar(icons = {
        IconButton(onClick = openDrawer) {
            Icon(
                imageVector = Icons.Rounded.Menu,
                contentDescription = stringResource(id = string.menu_button_description)
            )
        }
    })
}

object HomeScreen {

    const val TestTag = "HomeScreen"
}

@Composable
@Preview(showBackground = true)
private fun HomeDrawerContentPreview() {
    CineScoutTheme {
        HomeDrawerContent()
    }
}

@Composable
@Preview(showBackground = true)
private fun HomeScreenPreview() {
    CineScoutTheme {
        HomeScreen()
    }
}
