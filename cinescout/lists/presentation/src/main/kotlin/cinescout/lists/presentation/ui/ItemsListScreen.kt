package cinescout.lists.presentation.ui

import androidx.compose.animation.Crossfade
import androidx.compose.foundation.ExperimentalFoundationApi
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
import androidx.compose.ui.tooling.preview.Devices
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.paging.compose.LazyPagingItems
import cinescout.design.TestTag
import cinescout.design.theme.CineScoutTheme
import cinescout.design.theme.Dimens
import cinescout.design.theme.imageBackground
import cinescout.design.ui.CenteredProgress
import cinescout.design.ui.ErrorScreen
import cinescout.design.ui.FailureImage
import cinescout.design.util.collectAsStateLifecycleAware
import cinescout.lists.presentation.action.ItemsListAction
import cinescout.lists.presentation.model.ListItemUiModel
import cinescout.lists.presentation.previewdata.ItemsListScreenPreviewDataProvider
import cinescout.lists.presentation.state.ItemsListState
import cinescout.lists.presentation.viewmodel.ItemsListViewModel
import cinescout.media.domain.model.asPosterRequest
import cinescout.resources.R.drawable
import cinescout.resources.TextRes
import cinescout.screenplay.domain.model.TmdbScreenplayId
import cinescout.utils.compose.Consume
import cinescout.utils.compose.items
import cinescout.utils.compose.paging.PagingItemsState
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
    Consume(state.errorMessage) { actions.onError(it) }

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
        ListContent(
            emptyMessage = state.emptyMessage,
            itemsState = state.itemsState,
            actions = actions,
            gridState = gridState
        )
    }
}

@Composable
private fun ListContent(
    emptyMessage: TextRes,
    itemsState: PagingItemsState<ListItemUiModel>,
    actions: ItemsListScreen.Actions,
    gridState: LazyGridState
) {
    Crossfade(targetState = itemsState, label = "ListContent") { state ->
        when (state) {
            PagingItemsState.Empty -> ErrorScreen(text = emptyMessage)
            is PagingItemsState.Error -> ErrorScreen(text = state.message)
            PagingItemsState.Loading -> CenteredProgress()
            is PagingItemsState.NotEmpty -> NotEmptyListContent(
                items = state.items,
                actions = actions,
                gridState = gridState
            )
        }
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
private fun NotEmptyListContent(
    items: LazyPagingItems<ListItemUiModel>,
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
        items(items = items, key = { it.tmdbId.uniqueId() }) { item ->
            if (item != null) {
                ListItem(
                    modifier = Modifier.testTag(item.title).animateItemPlacement(),
                    model = item,
                    actions = actions
                )
            } else {
                Text("Loading...")
            }
        }
    }
}

@Composable
private fun ListItem(
    model: ListItemUiModel,
    actions: ItemsListScreen.Actions,
    modifier: Modifier = Modifier
) {
    BoxWithConstraints(modifier = modifier.padding(Dimens.Margin.Small)) {
        ElevatedCard(modifier = Modifier.clickable { actions.toScreenplayDetails(model.tmdbId) }) {
            val imageWidth = this@BoxWithConstraints.maxWidth
            val imageHeight = imageWidth * 1.4f
            CoilImage(
                modifier = Modifier
                    .width(imageWidth)
                    .height(imageHeight)
                    .imageBackground(),
                imageModel = { model.tmdbId.asPosterRequest() },
                imageOptions = ImageOptions(contentScale = ContentScale.FillWidth),
                failure = { FailureImage() },
                loading = { CenteredProgress() },
                previewPlaceholder = drawable.img_poster
            )
        }
    }
}

object ItemsListScreen {

    data class Actions(
        val onError: @Composable (TextRes) -> Unit,
        val toScreenplayDetails: (screenplayId: TmdbScreenplayId) -> Unit
    ) {

        companion object {

            val Empty = Actions(onError = {}, toScreenplayDetails = {})
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
