package cinescout.lists.presentation.ui

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyGridState
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Devices
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import arrow.core.NonEmptyList
import cinescout.design.R.drawable
import cinescout.design.TestTag
import cinescout.design.theme.CineScoutTheme
import cinescout.design.theme.Dimens
import cinescout.design.theme.imageBackground
import cinescout.design.ui.CenteredProgress
import cinescout.design.ui.ErrorScreen
import cinescout.design.util.NoContentDescription
import cinescout.design.util.collectAsStateLifecycleAware
import cinescout.lists.presentation.action.ItemsListAction
import cinescout.lists.presentation.model.ListItemUiModel
import cinescout.lists.presentation.previewdata.ItemsListScreenPreviewDataProvider
import cinescout.lists.presentation.state.ItemsListState
import cinescout.lists.presentation.viewmodel.ItemsListViewModel
import cinescout.movies.domain.model.TmdbMovieId
import cinescout.tvshows.domain.model.TmdbTvShowId
import com.skydoves.landscapist.ImageOptions
import com.skydoves.landscapist.coil.CoilImage
import org.koin.androidx.compose.koinViewModel

@Composable
fun ItemsListScreen(actions: ItemsListScreen.Actions) {
    val viewModel: ItemsListViewModel = koinViewModel()
    val state by viewModel.state.collectAsStateLifecycleAware()

    ItemsListScreen(
        state = state,
        actions = actions,
        onOptionConfig = {
            viewModel.submit(ItemsListAction.SelectFilter(it.filter))
            viewModel.submit(ItemsListAction.SelectType(it.type))
        }
    )
}

@Composable
internal fun ItemsListScreen(
    state: ItemsListState,
    actions: ItemsListScreen.Actions,
    onOptionConfig: (ListOptions.Config) -> Unit,
    modifier: Modifier = Modifier
) {
    val gridState = rememberSaveable(state.type, saver = LazyGridState.Saver) {
        LazyGridState()
    }
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(start = Dimens.Margin.Small, end = Dimens.Margin.Small, top = Dimens.Margin.Small)
            .testTag(TestTag.MyLists),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        var optionsConfig by remember {
            mutableStateOf(
                ListOptions.Config(filter = state.filter, type = state.type)
            )
        }
        ListOptions(
            config = optionsConfig,
            onConfigChange = { config ->
                optionsConfig = config
                onOptionConfig(config)
            },
            modifier = Modifier.padding(horizontal = Dimens.Margin.XSmall)
        )
        when (state.items) {
            is ItemsListState.ItemsState.Error -> ErrorScreen(text = state.items.message)
            ItemsListState.ItemsState.Loading -> CenteredProgress()
            is ItemsListState.ItemsState.Data -> ListContent(
                data = state.items,
                actions = actions,
                gridState = gridState
            )
        }
    }
}

@Composable
private fun ListContent(
    data: ItemsListState.ItemsState.Data,
    actions: ItemsListScreen.Actions,
    gridState: LazyGridState
) {
    when (data) {
        is ItemsListState.ItemsState.Data.Empty -> ErrorScreen(text = data.message)
        is ItemsListState.ItemsState.Data.NotEmpty -> NotEmptyListContent(
            items = data.items,
            actions = actions,
            gridState = gridState
        )
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
private fun NotEmptyListContent(
    items: NonEmptyList<ListItemUiModel>,
    actions: ItemsListScreen.Actions,
    gridState: LazyGridState
) {
    val backgroundColor = MaterialTheme.colorScheme.background
    val shadowBrush by remember {
        derivedStateOf {
            val isScrolled = gridState.firstVisibleItemScrollOffset > 0
            val colors =
                if (isScrolled) listOf(backgroundColor) + (0..50).map { Color.Transparent }
                else (0..2).map { Color.Transparent }
            Brush.verticalGradient(colors)
        }
    }
    LazyVerticalGrid(
        modifier = Modifier.drawWithContent {
            drawContent()
            drawRect(shadowBrush)
        },
        state = gridState,
        columns = GridCells.Adaptive(minSize = Dimens.Component.XXLarge),
        contentPadding = PaddingValues(horizontal = Dimens.Margin.XSmall)
    ) {
        items(items = items, key = { it.tmdbIdValue }) { item ->
            ListItem(model = item, actions = actions, modifier = Modifier.animateItemPlacement())
        }
    }
}

@Composable
private fun ListItem(
    model: ListItemUiModel,
    actions: ItemsListScreen.Actions,
    modifier: Modifier = Modifier
) {
    BoxWithConstraints(modifier = modifier.padding(Dimens.Margin.XSmall)) {
        ElevatedCard(
            modifier = Modifier.clickable {
                when (model) {
                    is ListItemUiModel.Movie -> actions.toMovieDetails(model.tmdbId)
                    is ListItemUiModel.TvShow -> actions.toTvShowDetails(model.tmdbId)
                }
            }
        ) {
            Column {
                val imageWidth = this@BoxWithConstraints.maxWidth
                val imageHeight = imageWidth * 1.35f
                CoilImage(
                    modifier = Modifier
                        .width(imageWidth)
                        .height(imageHeight)
                        .imageBackground(),
                    imageModel = { model.posterUrl },
                    imageOptions = ImageOptions(contentScale = ContentScale.FillWidth),
                    failure = {
                        Image(
                            painter = painterResource(id = drawable.ic_warning_30),
                            contentDescription = NoContentDescription
                        )
                    },
                    previewPlaceholder = drawable.img_poster
                )
                Text(
                    modifier = Modifier.padding(vertical = Dimens.Margin.XXSmall, horizontal = Dimens.Margin.Small),
                    text = model.title,
                    style = MaterialTheme.typography.labelSmall,
                    maxLines = 2
                )
            }
        }
    }
}

object ItemsListScreen {

    data class Actions(
        val toMovieDetails: (movieId: TmdbMovieId) -> Unit,
        val toTvShowDetails: (tvShowId: TmdbTvShowId) -> Unit
    ) {

        companion object {

            val Empty = Actions(toMovieDetails = {}, toTvShowDetails = {})
        }
    }
}

@Composable
@Preview(showBackground = true)
@Preview(showSystemUi = true, device = Devices.TABLET)
private fun ItemsListScreenPreview(
    @PreviewParameter(ItemsListScreenPreviewDataProvider::class) state: ItemsListState
) {
    var currentState by remember { mutableStateOf(state) }
    CineScoutTheme {
        ItemsListScreen(
            state = currentState,
            actions = ItemsListScreen.Actions.Empty,
            onOptionConfig = { currentState = currentState.copy(filter = it.filter, type = it.type) }
        )
    }
}
