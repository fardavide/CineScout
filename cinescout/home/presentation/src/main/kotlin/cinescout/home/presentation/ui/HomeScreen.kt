package cinescout.home.presentation.ui

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.WindowInsetsSides
import androidx.compose.foundation.layout.only
import androidx.compose.foundation.layout.systemBars
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.TopAppBarScrollBehavior
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.compose.rememberNavController
import cinescout.design.ImageRes
import cinescout.design.NavHost
import cinescout.design.R.drawable
import cinescout.design.R.string
import cinescout.design.TestTag
import cinescout.design.TextRes
import cinescout.design.composable
import cinescout.design.model.NavigationItem
import cinescout.design.navigate
import cinescout.design.string
import cinescout.design.theme.CineScoutTheme
import cinescout.design.ui.CenteredProgress
import cinescout.design.ui.ConnectionStatusBanner
import cinescout.design.ui.FailureImage
import cinescout.design.ui.NavigationScaffold
import cinescout.design.util.collectAsStateLifecycleAware
import cinescout.home.presentation.HomeDestination
import cinescout.home.presentation.currentHomeDestinationAsState
import cinescout.home.presentation.state.HomeState
import cinescout.home.presentation.viewmodel.HomeViewModel
import cinescout.lists.presentation.ui.ItemsListScreen
import cinescout.movies.domain.model.TmdbMovieId
import cinescout.suggestions.presentation.ui.ForYouScreen
import cinescout.tvshows.domain.model.TmdbTvShowId
import cinescout.utils.compose.Adaptive
import cinescout.utils.compose.WindowWidthSizeClass
import com.skydoves.landscapist.ImageOptions
import com.skydoves.landscapist.coil.CoilImage
import kotlinx.collections.immutable.persistentListOf
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
    val navController = rememberNavController()

    val currentHomeDestination by navController.currentHomeDestinationAsState()

    val navigationItems = persistentListOf(
        NavigationItem(
            icon = ImageRes(drawable.ic_magic_wand),
            selectedIcon = ImageRes(drawable.ic_magic_wand_filled),
            label = TextRes(string.suggestions_for_you),
            onClick = { navController.navigate(HomeDestination.ForYou) },
            isSelected = currentHomeDestination is HomeDestination.ForYou
        ),
        NavigationItem(
            icon = ImageRes(drawable.ic_list),
            selectedIcon = ImageRes(drawable.ic_list_filled),
            label = TextRes(string.lists_my_lists),
            onClick = { navController.navigate(HomeDestination.MyLists) },
            isSelected = currentHomeDestination is HomeDestination.MyLists
        ),
        NavigationItem(
            icon = ImageRes(drawable.ic_user),
            selectedIcon = ImageRes(drawable.ic_user_filled),
            label = TextRes(string.profile),
            onClick = actions.toProfile,
            isSelected = false
        )
    )

    val scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior()

    NavigationScaffold(
        modifier = modifier.testTag(TestTag.Home).nestedScroll(scrollBehavior.nestedScrollConnection),
        items = navigationItems,
        banner = { ConnectionStatusBanner(uiModel = state.connectionStatus) },
        topBar = {
            HomeTopBar(
                scrollBehavior = scrollBehavior,
                primaryAccount = state.account,
                currentDestination = currentHomeDestination,
                openAccounts = actions.toProfile
            )
        }
    ) {
        NavHost(navController = navController, startDestination = startDestination) {
            composable(HomeDestination.ForYou) {
                val forYouActions = ForYouScreen.Actions(
                    login = actions.toProfile,
                    toMovieDetails = actions.toMovieDetails,
                    toTvShowDetails = actions.toTvShowDetails
                )
                ForYouScreen(actions = forYouActions)
            }
            composable(HomeDestination.MyLists) {
                val myListsActions = ItemsListScreen.Actions(
                    toMovieDetails = actions.toMovieDetails,
                    toTvShowDetails = actions.toTvShowDetails
                )
                ItemsListScreen(myListsActions)
            }
            composable(HomeDestination.None) {}
        }
    }
}

@Composable
private fun HomeTopBar(
    scrollBehavior: TopAppBarScrollBehavior,
    primaryAccount: HomeState.Account,
    currentDestination: HomeDestination,
    openAccounts: () -> Unit
) {
    Adaptive { windowSizeClass ->
        CenterAlignedTopAppBar(
            scrollBehavior = scrollBehavior,
            colors = TopAppBarDefaults.centerAlignedTopAppBarColors(containerColor = Color.Transparent),
            title = {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text(text = string(textRes = currentDestination.label))
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
                                failure = { FailureImage() },
                                loading = { CenteredProgress() },
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

object HomeScreen {

    data class Actions(
        val toProfile: () -> Unit,
        val toMovieDetails: (movieId: TmdbMovieId) -> Unit,
        val toTvShowDetails: (tvShowId: TmdbTvShowId) -> Unit
    ) {

        companion object {

            val Empty = Actions(toProfile = {}, toMovieDetails = {}, toTvShowDetails = {})
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
