package cinescout.details.presentation.ui

import android.content.Intent
import android.net.Uri
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.calculateEndPadding
import androidx.compose.foundation.layout.calculateStartPadding
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.ArrowBack
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Devices
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import cinescout.design.TestTag
import cinescout.design.theme.CineScoutTheme
import cinescout.design.theme.Dimens
import cinescout.design.theme.imageBackground
import cinescout.design.ui.BannerScaffold
import cinescout.design.ui.CenteredProgress
import cinescout.design.ui.CineScoutBottomBar
import cinescout.design.ui.ConnectionStatusBanner
import cinescout.design.ui.ErrorScreen
import cinescout.design.ui.FailureImage
import cinescout.design.util.collectAsStateLifecycleAware
import cinescout.details.presentation.action.ScreenplayDetailsAction
import cinescout.details.presentation.model.ScreenplayDetailsUiModel
import cinescout.details.presentation.previewdata.ScreenplayDetailsScreenPreviewDataProvider
import cinescout.details.presentation.state.ScreenplayDetailsItemState
import cinescout.details.presentation.state.ScreenplayDetailsState
import cinescout.details.presentation.ui.component.ScreenplayRatings
import cinescout.details.presentation.viewmodel.ScreenplayDetailsViewModel
import cinescout.resources.R.drawable
import cinescout.resources.R.string
import cinescout.screenplay.domain.model.Rating
import cinescout.screenplay.domain.model.ScreenplayIds
import cinescout.utils.compose.Adaptive
import co.touchlab.kermit.Logger
import com.google.accompanist.flowlayout.FlowMainAxisAlignment
import com.google.accompanist.flowlayout.FlowRow
import com.skydoves.landscapist.ImageOptions
import com.skydoves.landscapist.coil.CoilImage
import dev.chrisbanes.snapper.ExperimentalSnapperApi
import dev.chrisbanes.snapper.rememberSnapperFlingBehavior
import kotlinx.collections.immutable.ImmutableList
import org.koin.androidx.compose.koinViewModel
import org.koin.core.parameter.parametersOf

@Composable
fun ScreenplayDetailsScreen(
    screenplayIds: ScreenplayIds,
    actions: ScreenplayDetailsScreen.Actions,
    modifier: Modifier = Modifier
) {
    val viewModel: ScreenplayDetailsViewModel = koinViewModel(parameters = { parametersOf(screenplayIds) })
    val state by viewModel.state.collectAsStateLifecycleAware()
    val screenplayActions = ScreenplayDetailsScreen.ScreenplayActions(
        addToWatchlist = { viewModel.submit(ScreenplayDetailsAction.AddToWatchlist) },
        rate = { viewModel.submit(ScreenplayDetailsAction.Rate(it)) },
        removeFromWatchlist = { viewModel.submit(ScreenplayDetailsAction.RemoveFromWatchlist) }
    )
    ScreenplayDetailsScreen(
        state = state,
        actions = actions,
        screenplayActions = screenplayActions,
        modifier = modifier
    )
}

@Composable
fun ScreenplayDetailsScreen(
    state: ScreenplayDetailsState,
    actions: ScreenplayDetailsScreen.Actions,
    screenplayActions: ScreenplayDetailsScreen.ScreenplayActions,
    modifier: Modifier = Modifier
) {
    Logger.withTag("ScreenplayDetailsScreen").d("State: $state")
    BannerScaffold(
        modifier = modifier.testTag(TestTag.ScreenplayDetails),
        bottomBar = {
            val isInWatchlist = (state.itemState as? ScreenplayDetailsItemState.Data)?.uiModel?.isInWatchlist
            val bottomBarActions = MovieDetailsBottomBar.Actions(
                onBack = actions.onBack,
                addToWatchlist = screenplayActions.addToWatchlist,
                removeFromWatchlist = screenplayActions.removeFromWatchlist
            )
            MovieDetailsBottomBar(isInWatchlist = isInWatchlist, actions = bottomBarActions)
        },
        banner = { ConnectionStatusBanner(uiModel = state.connectionStatus) }
    ) { paddingValues ->
        val layoutDirection = LocalLayoutDirection.current
        Surface(
            modifier = Modifier.padding(
                start = paddingValues.calculateStartPadding(layoutDirection),
                end = paddingValues.calculateEndPadding(layoutDirection),
                bottom = paddingValues.calculateBottomPadding()
            )
        ) {
            MovieDetailsContent(state = state.itemState, screenplayActions = screenplayActions)
        }
    }
}

@Composable
internal fun MovieDetailsContent(
    state: ScreenplayDetailsItemState,
    screenplayActions: ScreenplayDetailsScreen.ScreenplayActions
) {
    var shouldShowRateDialog by remember { mutableStateOf(false) }
    when (state) {
        is ScreenplayDetailsItemState.Data -> {
            val uiModel = state.uiModel
            if (shouldShowRateDialog) {
                val dialogActions = RateItemDialog.Actions(
                    onDismissRequest = { shouldShowRateDialog = false },
                    saveRating = screenplayActions.rate
                )
                RateItemDialog(
                    itemTitle = uiModel.title,
                    itemPersonalRating = uiModel.ratings.personal.rating,
                    actions = dialogActions
                )
            }
            Adaptive { windowSizeClass ->
                val mode = ScreenplayDetailsLayout.Mode.forClass(windowSizeClass)
                ScreenplayDetailsLayout(
                    mode = mode,
                    backdrops = { Backdrops(urls = uiModel.backdrops) },
                    poster = { Poster(url = uiModel.posterUrl) },
                    infoBox = { InfoBox(title = uiModel.title, releaseDate = uiModel.releaseDate) },
                    ratings = {
                        ScreenplayRatings(
                            ratings = uiModel.ratings,
                            openRateDialog = { shouldShowRateDialog = true }
                        )
                    },
                    genres = { Genres(mode = mode, genres = uiModel.genres) },
                    credits = { CreditsMembers(mode = mode, creditsMembers = uiModel.creditsMember) },
                    overview = { Overview(overview = uiModel.overview) },
                    videos = { Videos(videos = uiModel.videos) }
                )
            }
        }
        is ScreenplayDetailsItemState.Error -> ErrorScreen(text = state.message)
        ScreenplayDetailsItemState.Loading -> CenteredProgress()
    }
}

@Composable
@OptIn(ExperimentalSnapperApi::class)
private fun Backdrops(urls: ImmutableList<String?>) {
    val lazyListState = rememberLazyListState()
    Box(contentAlignment = Alignment.TopCenter) {
        LazyRow(
            state = lazyListState,
            flingBehavior = rememberSnapperFlingBehavior(lazyListState)
        ) {
            items(urls) { url ->
                Backdrop(modifier = Modifier.fillParentMaxSize(), url = url)
            }
        }
        Row(modifier = Modifier.statusBarsPadding()) {
            val currentIndex by remember { derivedStateOf { lazyListState.firstVisibleItemIndex } }
            repeat(urls.size) { index ->
                fun Modifier.background() = when (index) {
                    currentIndex -> background(color = Color.Transparent)
                    else -> background(color = Color.White, shape = CircleShape)
                }

                fun Modifier.border() = when (index) {
                    currentIndex -> border(width = Dimens.Outline, color = Color.White, shape = CircleShape)
                    else -> border(width = Dimens.Outline, color = Color.Black)
                }
                Box(
                    modifier = Modifier
                        .padding(Dimens.Margin.XXSmall)
                        .size(Dimens.Indicator)
                        .background()
                        .border()
                )
            }
        }
    }
}

@Composable
private fun Backdrop(url: String?, modifier: Modifier = Modifier) {
    CoilImage(
        modifier = modifier
            .fillMaxSize()
            .imageBackground(),
        imageModel = { url },
        imageOptions = ImageOptions(contentScale = ContentScale.Crop),
        failure = { FailureImage() },
        loading = { CenteredProgress() },
        previewPlaceholder = drawable.img_backdrop
    )
}

@Composable
private fun Poster(url: String?) {
    CoilImage(
        modifier = Modifier
            .fillMaxSize()
            .clip(MaterialTheme.shapes.medium)
            .imageBackground(),
        imageModel = { url },
        failure = { FailureImage() },
        loading = { CenteredProgress() },
        previewPlaceholder = drawable.img_poster
    )
}

@Composable
private fun InfoBox(title: String, releaseDate: String) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(
                color = MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.55f),
                shape = MaterialTheme.shapes.medium
            )
            .padding(Dimens.Margin.Small)
    ) {
        Text(text = title, maxLines = 2, style = MaterialTheme.typography.titleMedium)
        Spacer(Modifier.height(Dimens.Margin.Small))
        Text(text = releaseDate, style = MaterialTheme.typography.labelMedium)
    }
}

@Composable
private fun Genres(mode: ScreenplayDetailsLayout.Mode, genres: ImmutableList<String>) {
    when (mode) {
        ScreenplayDetailsLayout.Mode.Horizontal -> FlowRow(mainAxisAlignment = FlowMainAxisAlignment.Center) {
            for (genre in genres) {
                Genre(genre = genre)
            }
        }
        is ScreenplayDetailsLayout.Mode.Vertical -> LazyRow {
            items(genres) { genre ->
                Genre(genre = genre)
            }
        }
    }
}

@Composable
private fun Genre(genre: String) {
    Text(
        modifier = Modifier
            .padding(Dimens.Margin.XXSmall)
            .background(
                color = MaterialTheme.colorScheme.tertiary.copy(alpha = 0.35f),
                shape = MaterialTheme.shapes.small
            )
            .padding(Dimens.Margin.Small),
        text = genre,
        style = MaterialTheme.typography.labelLarge
    )
}

@Composable
private fun CreditsMembers(
    mode: ScreenplayDetailsLayout.Mode,
    creditsMembers: ImmutableList<ScreenplayDetailsUiModel.CreditsMember>
) {
    when (mode) {
        ScreenplayDetailsLayout.Mode.Horizontal -> LazyColumn(
            contentPadding = PaddingValues(vertical = Dimens.Margin.Small)
        ) {
            items(creditsMembers) { member ->
                CreditsMember(mode = mode, member = member)
            }
        }
        is ScreenplayDetailsLayout.Mode.Vertical -> LazyRow {
            items(creditsMembers) { member ->
                CreditsMember(mode = mode, member = member)
            }
        }
    }
}

@Composable
private fun CreditsMember(
    mode: ScreenplayDetailsLayout.Mode,
    member: ScreenplayDetailsUiModel.CreditsMember
) {
    when (mode) {
        ScreenplayDetailsLayout.Mode.Horizontal -> Row(
            modifier = Modifier.padding(vertical = Dimens.Margin.XSmall),
            verticalAlignment = Alignment.CenterVertically
        ) {
            CreditsMemberImage(url = member.profileImageUrl)
            Spacer(modifier = Modifier.width(Dimens.Margin.Small))
            Column {
                Text(
                    modifier = Modifier.fillMaxWidth(),
                    text = member.name,
                    style = MaterialTheme.typography.labelSmall,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis
                )
                Text(
                    modifier = Modifier.fillMaxWidth(),
                    text = member.role.orEmpty(),
                    style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Light),
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis
                )
            }
        }
        is ScreenplayDetailsLayout.Mode.Vertical -> Column(
            modifier = Modifier
                .width(Dimens.Image.Medium + Dimens.Margin.Medium * 2)
                .padding(Dimens.Margin.XSmall),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            CreditsMemberImage(url = member.profileImageUrl)
            Spacer(modifier = Modifier.height(Dimens.Margin.XSmall))
            Text(
                modifier = Modifier.fillMaxWidth(),
                text = member.name,
                style = MaterialTheme.typography.labelSmall,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
            Text(
                modifier = Modifier.fillMaxWidth(),
                text = member.role.orEmpty(),
                style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Light),
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
        }
    }
}

@Composable
private fun CreditsMemberImage(url: String?) {
    CoilImage(
        modifier = Modifier
            .size(Dimens.Image.Medium)
            .clip(CircleShape)
            .imageBackground(),
        imageModel = { url },
        imageOptions = ImageOptions(contentScale = ContentScale.Crop),
        failure = { FailureImage() },
        previewPlaceholder = drawable.ic_user_color
    )
}

@Composable
private fun Overview(overview: String) {
    Text(
        modifier = Modifier.fillMaxWidth(),
        text = overview,
        style = MaterialTheme.typography.bodyMedium
    )
}

@Composable
private fun Videos(videos: ImmutableList<ScreenplayDetailsUiModel.Video>) {
    val context = LocalContext.current

    BoxWithConstraints(modifier = Modifier.fillMaxWidth()) {
        LazyRow {
            items(videos) { video ->
                Column(
                    modifier = Modifier
                        .width(maxWidth * 47 / 100)
                        .padding(horizontal = Dimens.Margin.XSmall)
                        .clickable {
                            context.startActivity(
                                Intent(
                                    Intent.ACTION_VIEW,
                                    Uri.parse(video.url)
                                )
                            )
                        }
                ) {
                    CoilImage(
                        modifier = Modifier
                            .clip(MaterialTheme.shapes.medium)
                            .imageBackground(),
                        imageModel = { video.previewUrl },
                        imageOptions = ImageOptions(contentDescription = video.title),
                        previewPlaceholder = drawable.img_video
                    )
                    Spacer(modifier = Modifier.height(Dimens.Margin.XSmall))
                    Text(
                        text = video.title,
                        maxLines = 2,
                        style = MaterialTheme.typography.labelMedium,
                        overflow = TextOverflow.Ellipsis
                    )
                }
            }
        }
    }
}

@Composable
private fun MovieDetailsBottomBar(isInWatchlist: Boolean?, actions: MovieDetailsBottomBar.Actions) {
    CineScoutBottomBar(
        icon = {
            IconButton(onClick = actions.onBack) {
                Icon(
                    imageVector = Icons.Rounded.ArrowBack,
                    contentDescription = stringResource(id = string.back_button_description)
                )
            }
        },
        actions = {
            when (isInWatchlist) {
                true -> IconButton(onClick = actions.removeFromWatchlist) {
                    Icon(
                        painter = painterResource(id = drawable.ic_bookmark_filled),
                        tint = MaterialTheme.colorScheme.primary,
                        contentDescription = stringResource(id = string.remove_from_watchlist_button_description)
                    )
                }

                false -> IconButton(onClick = actions.addToWatchlist) {
                    Icon(
                        painter = painterResource(id = drawable.ic_bookmark),
                        contentDescription = stringResource(id = string.add_to_watchlist_button_description)
                    )
                }

                null -> Unit
            }
        }
    )
}

object ScreenplayDetailsScreen {

    const val ScreenplayIdsKey = "screenplay_ids"

    data class Actions(
        val onBack: () -> Unit
    ) {

        companion object {

            val Empty = Actions(onBack = {})
        }
    }

    data class ScreenplayActions(
        val addToWatchlist: () -> Unit,
        val rate: (Rating) -> Unit,
        val removeFromWatchlist: () -> Unit
    ) {

        companion object {

            val Empty = ScreenplayActions(
                addToWatchlist = {},
                rate = {},
                removeFromWatchlist = {}
            )
        }
    }
}

private object MovieDetailsBottomBar {

    data class Actions(
        val onBack: () -> Unit,
        val addToWatchlist: () -> Unit,
        val removeFromWatchlist: () -> Unit
    )
}

@Composable
@Preview
@Preview(device = Devices.TABLET)
private fun ScreenplayDetailsScreenPreview(
    @PreviewParameter(ScreenplayDetailsScreenPreviewDataProvider::class) state: ScreenplayDetailsState
) {
    CineScoutTheme {
        ScreenplayDetailsScreen(
            state = state,
            actions = ScreenplayDetailsScreen.Actions.Empty,
            screenplayActions = ScreenplayDetailsScreen.ScreenplayActions.Empty
        )
    }
}
