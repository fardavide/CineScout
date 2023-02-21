package cinescout.home.presentation.ui

import android.content.ActivityNotFoundException
import android.content.ClipData
import android.content.ClipboardManager
import android.content.Intent
import android.net.Uri
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.WindowInsetsSides
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.only
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.systemBars
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Menu
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.core.content.getSystemService
import androidx.navigation.compose.rememberNavController
import cinescout.design.NavHost
import cinescout.design.R.drawable
import cinescout.design.R.string
import cinescout.design.TestTag
import cinescout.design.composable
import cinescout.design.navigate
import cinescout.design.string
import cinescout.design.theme.CineScoutTheme
import cinescout.design.ui.CineScoutBottomBar
import cinescout.design.ui.ConnectionStatusBanner
import cinescout.design.ui.DrawerScaffold
import cinescout.design.util.Consume
import cinescout.design.util.NoContentDescription
import cinescout.design.util.collectAsStateLifecycleAware
import cinescout.home.presentation.HomeDestination
import cinescout.home.presentation.currentHomeDestinationAsState
import cinescout.home.presentation.state.HomeState
import cinescout.home.presentation.viewmodel.HomeViewModel
import cinescout.lists.presentation.ui.DislikedListScreen
import cinescout.lists.presentation.ui.ItemsListScreen
import cinescout.lists.presentation.ui.LikedListScreen
import cinescout.lists.presentation.ui.MyListsScreen
import cinescout.lists.presentation.ui.RatedListScreen
import cinescout.lists.presentation.ui.WatchlistScreen
import cinescout.movies.domain.model.TmdbMovieId
import cinescout.suggestions.presentation.ui.ForYouScreen
import cinescout.tvshows.domain.model.TmdbTvShowId
import cinescout.utils.compose.Adaptive
import cinescout.utils.compose.WindowWidthSizeClass
import com.skydoves.landscapist.ImageOptions
import com.skydoves.landscapist.coil.CoilImage
import kotlinx.coroutines.launch
import org.koin.androidx.compose.koinViewModel

@Composable
fun HomeScreen(actions: HomeScreen.Actions, modifier: Modifier = Modifier) {
    val viewModel: HomeViewModel = koinViewModel()
    val state by viewModel.state.collectAsStateLifecycleAware()
    HomeScreen(state = state, actions = actions, modifier = modifier)
}

@Composable
fun HomeScreen(
    state: HomeState,
    actions: HomeScreen.Actions,
    modifier: Modifier = Modifier,
    startDestination: HomeDestination = HomeDestination.Start
) {
    val context = LocalContext.current
    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val navController = rememberNavController()
    val scope = rememberCoroutineScope()
    val snackbarHostState = SnackbarHostState()

    val onDrawerItemClick: (HomeDrawer.ItemId) -> Unit = { itemId ->
        when (itemId) {
            HomeDrawer.ItemId.ForYou -> navController.navigate(HomeDestination.ForYou)
            HomeDrawer.ItemId.MyLists -> navController.navigate(HomeDestination.MyLists)
            HomeDrawer.ItemId.Login -> actions.toManageAccount()
            HomeDrawer.ItemId.Watchlist -> navController.navigate(HomeDestination.Watchlist)
        }
        scope.launch { drawerState.close() }
    }

    Consume(effect = state.loginEffect) { loginState ->
        when (loginState) {
            is HomeState.Login.Error -> {
                val message = string(textRes = loginState.message)
                scope.launch { snackbarHostState.showSnackbar(message) }
            }

            HomeState.Login.Linked -> {
                val message = stringResource(id = string.manage_account_logged_in)
                scope.launch { snackbarHostState.showSnackbar(message) }
            }

            is HomeState.Login.UserShouldAuthorizeApp -> {
                val intent = Intent(Intent.ACTION_VIEW).setData(Uri.parse(loginState.authorizationUrl))
                try {
                    context.startActivity(intent)
                } catch (e: ActivityNotFoundException) {
                    val clipboardManager = context.getSystemService<ClipboardManager>()
                    val clipData = ClipData.newPlainText(
                        stringResource(id = string.login_authorization_url_clipboard_label),
                        loginState.authorizationUrl
                    )
                    clipboardManager?.setPrimaryClip(clipData)
                    val message = stringResource(id = string.login_error_cannot_open_browser)
                    scope.launch { snackbarHostState.showSnackbar(message, duration = SnackbarDuration.Long) }
                }
            }
        }
    }

    val currentHomeDestination by navController.currentHomeDestinationAsState()
    DrawerScaffold(
        modifier = modifier.testTag(TestTag.Home),
        drawerState = drawerState,
        drawerContent = { HomeDrawerContent(homeState = state, onItemClick = onDrawerItemClick) },
        bottomBar = { HomeBottomBar(openDrawer = { scope.launch { drawerState.open() } }) },
        snackbarHost = { SnackbarHost(snackbarHostState) },
        topBar = {
            HomeTopBar(
                primaryAccount = state.account,
                currentDestination = currentHomeDestination,
                openAccounts = actions.toManageAccount
            )
        },
        banner = { ConnectionStatusBanner(uiModel = state.connectionStatus) }
    ) { paddingValues ->
        Surface(
            modifier = Modifier
                .padding(paddingValues)
                .fillMaxSize()
        ) {
            NavHost(navController = navController, startDestination = startDestination) {
                val itemsListActions = ItemsListScreen.Actions(
                    toMovieDetails = actions.toMovieDetails,
                    toTvShowDetails = actions.toTvShowDetails
                )

                composable(HomeDestination.Disliked) {
                    DislikedListScreen(actions = itemsListActions)
                }
                composable(HomeDestination.ForYou) {
                    val forYouActions = ForYouScreen.Actions(
                        login = actions.toManageAccount,
                        toMovieDetails = actions.toMovieDetails,
                        toTvShowDetails = actions.toTvShowDetails
                    )
                    ForYouScreen(actions = forYouActions)
                }
                composable(HomeDestination.Liked) {
                    LikedListScreen(actions = itemsListActions)
                }
                composable(HomeDestination.MyLists) {
                    val myListsActions = MyListsScreen.Actions(
                        onDislikedClick = { navController.navigate(HomeDestination.Disliked) },
                        onLikedClick = { navController.navigate(HomeDestination.Liked) },
                        onRatedClick = { navController.navigate(HomeDestination.Rated) }
                    )
                    MyListsScreen(myListsActions)
                }
                composable(HomeDestination.None) {}
                composable(HomeDestination.Rated) {
                    RatedListScreen(actions = itemsListActions)
                }
                composable(HomeDestination.Watchlist) {
                    WatchlistScreen(actions = itemsListActions)
                }
            }
        }
    }
}

@Composable
private fun HomeTopBar(
    primaryAccount: HomeState.Account,
    currentDestination: HomeDestination,
    openAccounts: () -> Unit
) {
    Adaptive { windowSizeClass ->
        val containerColor = when (windowSizeClass.width) {
            WindowWidthSizeClass.Compact,
            WindowWidthSizeClass.Medium -> Color.Transparent
            WindowWidthSizeClass.Expanded -> MaterialTheme.colorScheme.surfaceVariant
        }
        CenterAlignedTopAppBar(
            colors = TopAppBarDefaults.centerAlignedTopAppBarColors(containerColor = containerColor),
            title = {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text(text = stringResource(id = string.app_name))
                    Text(
                        text = string(textRes = currentDestination.label),
                        style = MaterialTheme.typography.labelMedium
                    )
                }
            },
            actions = {
                if (windowSizeClass.width != WindowWidthSizeClass.Expanded) {
                    if (primaryAccount is HomeState.Account.Connected) {
                        IconButton(onClick = openAccounts) {
                            CoilImage(
                                modifier = Modifier.clip(CircleShape),
                                imageModel = { primaryAccount.uiModel.imageUrl },
                                imageOptions = ImageOptions(
                                    contentDescription = stringResource(id = string.profile_picture_description)
                                ),
                                failure = {
                                    Image(
                                        painter = painterResource(id = drawable.ic_user_color),
                                        contentDescription = NoContentDescription
                                    )
                                },
                                previewPlaceholder = drawable.ic_user_color
                            )
                        }
                    }
                }
            },
            windowInsets = WindowInsets.systemBars.only(WindowInsetsSides.Horizontal)
        )
    }
}

@Composable
private fun HomeBottomBar(openDrawer: () -> Unit) {
    Adaptive { windowSizeClass ->
        if (windowSizeClass.width != WindowWidthSizeClass.Expanded) {
            CineScoutBottomBar(
                icon = {
                    IconButton(onClick = openDrawer) {
                        Icon(
                            imageVector = Icons.Rounded.Menu,
                            contentDescription = stringResource(id = string.menu_button_description)
                        )
                    }
                }
            )
        }
    }
}

object HomeScreen {

    data class Actions(
        val toManageAccount: () -> Unit,
        val toMovieDetails: (movieId: TmdbMovieId) -> Unit,
        val toTvShowDetails: (tvShowId: TmdbTvShowId) -> Unit
    ) {

        companion object {

            val Empty = Actions(toManageAccount = {}, toMovieDetails = {}, toTvShowDetails = {})
        }
    }
}

@Composable
@Preview(showBackground = true)
private fun HomeScreenPreview() {
    CineScoutTheme {
        HomeScreen(
            state = HomeState.Loading,
            actions = HomeScreen.Actions.Empty,
            startDestination = HomeDestination.ForYou
        )
    }
}
