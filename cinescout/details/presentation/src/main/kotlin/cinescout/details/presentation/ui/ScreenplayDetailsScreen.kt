package cinescout.details.presentation.ui

import android.content.Intent
import android.net.Uri
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
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
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Devices
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import arrow.core.getOrElse
import cinescout.design.TestTag
import cinescout.design.theme.CineScoutTheme
import cinescout.design.theme.Dimens
import cinescout.design.theme.imageBackground
import cinescout.design.ui.BannerScaffold
import cinescout.design.ui.CenteredProgress
import cinescout.design.ui.ConnectionStatusBanner
import cinescout.design.ui.CsAssistChip
import cinescout.design.ui.ErrorScreen
import cinescout.design.ui.FailureImage
import cinescout.design.util.collectAsStateLifecycleAware
import cinescout.details.presentation.action.ScreenplayDetailsAction
import cinescout.details.presentation.model.ScreenplayDetailsUiModel
import cinescout.details.presentation.previewdata.ScreenplayDetailsScreenPreviewDataProvider
import cinescout.details.presentation.state.ScreenplayDetailsItemState
import cinescout.details.presentation.state.ScreenplayDetailsState
import cinescout.details.presentation.ui.component.DetailsActionBar
import cinescout.details.presentation.ui.component.DetailsBottomBar
import cinescout.details.presentation.ui.component.DetailsSideBar
import cinescout.details.presentation.ui.component.DetailsTopBar
import cinescout.details.presentation.viewmodel.ScreenplayDetailsViewModel
import cinescout.resources.R.drawable
import cinescout.resources.R.string
import cinescout.resources.TextRes
import cinescout.resources.string
import cinescout.screenplay.domain.model.Rating
import cinescout.screenplay.domain.model.ScreenplayType
import cinescout.screenplay.domain.model.ids.ScreenplayIds
import cinescout.screenplay.presentation.ui.ScreenplayTypeBadge
import cinescout.utils.compose.Adaptive
import cinescout.utils.compose.WindowWidthSizeClass
import co.touchlab.kermit.Logger
import com.google.accompanist.flowlayout.FlowMainAxisAlignment
import com.google.accompanist.flowlayout.FlowRow
import com.skydoves.landscapist.ImageOptions
import com.skydoves.landscapist.coil.CoilImage
import dev.chrisbanes.snapper.rememberSnapperFlingBehavior
import kotlinx.collections.immutable.ImmutableList
import kotlinx.coroutines.launch
import org.koin.androidx.compose.koinViewModel
import org.koin.core.parameter.parametersOf

@Composable
fun ScreenplayDetailsScreen(
    screenplayIds: ScreenplayIds,
    actions: ScreenplayDetailsScreen.Actions,
    modifier: Modifier = Modifier
) {
    val viewModel: ScreenplayDetailsViewModel = koinViewModel(parameters = {
        parametersOf(screenplayIds)
    })
    val state by viewModel.state.collectAsStateLifecycleAware()
    val screenplayActions = ScreenplayDetailsScreen.ScreenplayActions(
        addToHistory = { viewModel.submit(ScreenplayDetailsAction.AddToHistory) },
        rate = { viewModel.submit(ScreenplayDetailsAction.Rate(it)) },
        toggleWatchlist = { viewModel.submit(ScreenplayDetailsAction.ToggleWatchlist) }
    )
    ScreenplayDetailsScreen(
        state = state,
        actions = actions,
        screenplayActions = screenplayActions,
        modifier = modifier
    )
}

@Composable
internal fun ScreenplayDetailsScreen(
    state: ScreenplayDetailsState,
    actions: ScreenplayDetailsScreen.Actions,
    screenplayActions: ScreenplayDetailsScreen.ScreenplayActions,
    modifier: Modifier = Modifier
) {
    Logger.withTag("ScreenplayDetailsScreen").d("State: $state")
    val scope = rememberCoroutineScope()
    val snackbarHostState = SnackbarHostState()
    val topAppBarScrollBehavior = TopAppBarDefaults.exitUntilCollapsedScrollBehavior()

    val comingSoonMessage = stringResource(id = string.coming_soon)
    val comingSoon = {
        scope.launch { snackbarHostState.showSnackbar(comingSoonMessage) }
    }

    var shouldShowRateModal by remember { mutableStateOf(false) }
    var shouldShowAddToHistoryModal by remember { mutableStateOf(false) }

    val itemState = state.itemState

    if (shouldShowRateModal && itemState is ScreenplayDetailsItemState.Data) {
        val modalActions = RateItemModal.Actions(
            dismiss = { shouldShowRateModal = false },
            saveRating = screenplayActions.rate
        )
        RateItemModal(
            itemTitle = itemState.uiModel.title,
            itemPersonalRating = itemState.uiModel.personalRating.getOrElse { 0 },
            actions = modalActions
        )
    }
    if (shouldShowAddToHistoryModal && itemState is ScreenplayDetailsItemState.Data) {
        val modalActions = AddToHistoryModal.Actions(
            dismiss = { shouldShowAddToHistoryModal = false },
            addToHistory = screenplayActions.addToHistory
        )
        AddToHistoryModal(
            itemTitle = itemState.uiModel.title,
            actions = modalActions
        )
    }

    val barActions = DetailsActionBar.Actions(
        back = actions.back,
        openAddToHistory = { shouldShowAddToHistoryModal = true },
        openEdit = { comingSoon() },
        openRating = { shouldShowRateModal = true },
        toggleWatchlist = screenplayActions.toggleWatchlist
    )

    BannerScaffold(
        modifier = modifier
            .testTag(TestTag.ScreenplayDetails)
            .nestedScroll(topAppBarScrollBehavior.nestedScrollConnection),
        banner = { ConnectionStatusBanner(uiModel = state.connectionStatus) },
        topBar = { windowSizeClass ->
            if (windowSizeClass.width != WindowWidthSizeClass.Expanded) {
                DetailsTopBar(
                    back = actions.back,
                    scrollBehavior = topAppBarScrollBehavior
                )
            }
        },
        sideRail = { windowSizeClass ->
            if (windowSizeClass.width == WindowWidthSizeClass.Expanded) {
                DetailsSideBar(uiModel = state.actionsUiModel, actions = barActions)
            }
        },
        bottomBar = { windowSizeClass ->
            if (windowSizeClass.width != WindowWidthSizeClass.Expanded) {
                DetailsBottomBar(uiModel = state.actionsUiModel, actions = barActions)
            }
        },
        snackbarHost = { SnackbarHost(snackbarHostState) }
    ) { paddingValues ->
        val layoutDirection = LocalLayoutDirection.current
        Surface(
            modifier = Modifier.padding(
                start = paddingValues.calculateStartPadding(layoutDirection),
                end = paddingValues.calculateEndPadding(layoutDirection),
                bottom = paddingValues.calculateBottomPadding()
            )
        ) {
            ScreenplayDetailsContent(state = state.itemState)
        }
    }
}

@Composable
internal fun ScreenplayDetailsContent(state: ScreenplayDetailsItemState) {
    when (state) {
        is ScreenplayDetailsItemState.Data -> {
            val uiModel = state.uiModel
            Adaptive { windowSizeClass ->
                val mode = ScreenplayDetailsLayout.Mode.forClass(windowSizeClass)
                ScreenplayDetailsLayout(
                    mode = mode,
                    backdrops = { Backdrops(urls = uiModel.backdrops) },
                    typeBadge = { ScreenplayTypeBadge(type = ScreenplayType.from(uiModel.ids)) },
                    poster = { Poster(url = uiModel.posterUrl) },
                    infoBox = {
                        InfoBox(
                            title = uiModel.title,
                            releaseDate = uiModel.releaseDate,
                            runtime = uiModel.runtime
                        )
                    },
                    ratings = {},
                    genres = { Genres(mode = mode, genres = uiModel.genres) },
                    credits = {
                        CreditsMembers(mode = mode, creditsMembers = uiModel.creditsMember)
                    },
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
private fun InfoBox(
    title: String,
    releaseDate: String,
    runtime: TextRes?
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(
                color = MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.55f),
                shape = MaterialTheme.shapes.medium
            )
            .padding(Dimens.Margin.Small),
        verticalArrangement = Arrangement.spacedBy(Dimens.Margin.Small)
    ) {
        Text(text = title, maxLines = 2, style = MaterialTheme.typography.titleMedium)
        Text(text = releaseDate, style = MaterialTheme.typography.labelMedium)
        if (runtime != null) {
            Text(text = string(textRes = runtime), style = MaterialTheme.typography.labelMedium)
        }
    }
}

@Composable
private fun Genres(mode: ScreenplayDetailsLayout.Mode, genres: ImmutableList<String>) {
    val spacing = Dimens.Margin.XSmall
    when (mode) {
        ScreenplayDetailsLayout.Mode.Horizontal -> FlowRow(
            mainAxisAlignment = FlowMainAxisAlignment.Center,
            mainAxisSpacing = spacing,
            crossAxisSpacing = spacing
        ) {
            for (genre in genres) {
                Genre(genre = genre)
            }
        }
        is ScreenplayDetailsLayout.Mode.Vertical -> LazyRow(
            horizontalArrangement = Arrangement.spacedBy(spacing)
        ) {
            items(genres) { genre ->
                Genre(genre = genre)
            }
        }
    }
}

@Composable
private fun Genre(genre: String) {
    CsAssistChip { Text(text = genre) }
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

object ScreenplayDetailsScreen {

    const val ScreenplayIdsKey = "screenplay_ids"

    data class Actions(
        val back: () -> Unit
    ) {

        companion object {

            val Empty = Actions(back = {})
        }
    }

    data class ScreenplayActions(
        val addToHistory: () -> Unit,
        val rate: (Rating) -> Unit,
        val toggleWatchlist: () -> Unit
    ) {

        companion object {

            val Empty = ScreenplayActions(
                addToHistory = {},
                rate = {},
                toggleWatchlist = {}
            )
        }
    }
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
