package cinescout.home.presentation.ui

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Menu
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.Card
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
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
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.window.Dialog
import cinescout.design.theme.CineScoutTheme
import cinescout.design.theme.Dimens
import kotlinx.coroutines.launch
import studio.forface.cinescout.design.R.string

@Composable
fun HomeScreen(modifier: Modifier = Modifier) {
    val scope = rememberCoroutineScope()
    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    var shouldShowDialog by remember { mutableStateOf(false) }
    
    val onDrawerItemClick: (HomeDrawer.ItemId) -> Unit = { itemId ->
        when (itemId) {
            HomeDrawer.ItemId.Login -> shouldShowDialog = true
        }
    }
    
    if (shouldShowDialog) {
        LoginDialog(onDismissRequest = { shouldShowDialog = false })
    }
    
    HomeDrawer(drawerState = drawerState, onItemClick = onDrawerItemClick) {
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

@Composable
private fun LoginDialog(onDismissRequest: () -> Unit) {
    Dialog(onDismissRequest = onDismissRequest) {
        Card {
            Column(modifier = Modifier.padding(Dimens.Margin.Medium)) {
                Text(text = "Hello")
            }
        }
    }
}

object HomeScreen {

    const val TestTag = "HomeScreen"
}

@Composable
@Preview(showBackground = true)
private fun LoginDialogPreview() {
    CineScoutTheme {
        LoginDialog(onDismissRequest = {})
    }
}

@Composable
@Preview(showBackground = true)
private fun HomeScreenPreview() {
    CineScoutTheme {
        HomeScreen()
    }
}
