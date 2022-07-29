package cinescout.home.presentation.ui

import android.content.Intent
import android.net.Uri
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Menu
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import cinescout.design.TestTag
import cinescout.design.theme.CineScoutTheme
import cinescout.design.util.collectAsStateLifecycleAware
import cinescout.home.presentation.model.HomeAction
import cinescout.home.presentation.model.HomeState
import cinescout.home.presentation.viewmodel.HomeViewModel
import kotlinx.coroutines.launch
import org.koin.androidx.compose.koinViewModel
import studio.forface.cinescout.design.R.string

@Composable
fun HomeScreen(modifier: Modifier = Modifier) {
    val viewModel: HomeViewModel = koinViewModel()
    val state by viewModel.state.collectAsStateLifecycleAware()
    val loginActions = LoginActions(
        loginToTmdb = { viewModel.submit(HomeAction.LoginToTmdb) },
        loginToTrakt = { viewModel.submit(HomeAction.LoginToTrakt) }
    )
    HomeScreen(state = state, loginActions = loginActions, modifier = modifier)
}

@Composable
fun HomeScreen(state: HomeState, loginActions: LoginActions, modifier: Modifier = Modifier) {
    val context = LocalContext.current
    val scope = rememberCoroutineScope()
    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val snackbarHostState = SnackbarHostState()
    var shouldShowDialog by remember { mutableStateOf(false) }

    val onDrawerItemClick: (HomeDrawer.ItemId) -> Unit = { itemId ->
        when (itemId) {
            HomeDrawer.ItemId.Login -> {
                scope.launch { drawerState.close() }
                shouldShowDialog = true
            }
        }
    }

    when (state) {
        is HomeState.Error -> TODO()
        HomeState.Idle -> Unit
        HomeState.Linked -> {
            val message = stringResource(id = string.home_logged_in)
            LaunchedEffect(key1 = 0) {
                snackbarHostState.showSnackbar(message)
            }
        }
        is HomeState.UserShouldAuthorizeApp -> {
            LaunchedEffect(key1 = state.authorizationUrl) {
                context.startActivity(Intent(Intent.ACTION_VIEW).setData(Uri.parse(state.authorizationUrl)))
            }
        }
    }

    if (shouldShowDialog) {
        val action = LoginDialog.Actions(
            loginActions = loginActions,
            onDismissRequest = { shouldShowDialog = false }
        )
        LoginDialog(actions = action)
    }

    HomeDrawer(drawerState = drawerState, onItemClick = onDrawerItemClick) {
        Scaffold(
            modifier = modifier
                .statusBarsPadding()
                .navigationBarsPadding(),
            bottomBar = { HomeBottomBar(openDrawer = { scope.launch { drawerState.open() } }) },
            snackbarHost = { SnackbarHost(snackbarHostState) },
            topBar = { HomeTopBar() }
        ) { paddingValues ->
            Box(
                modifier = Modifier
                    .testTag(TestTag.Home)
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
@Preview(showBackground = true)
private fun HomeScreenPreview() {
    CineScoutTheme {
        HomeScreen()
    }
}
