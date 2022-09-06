package cinescout.lists.presentation.ui

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Devices
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import arrow.core.NonEmptyList
import cinescout.design.TextRes
import cinescout.design.theme.CineScoutTheme
import cinescout.design.theme.Dimens
import cinescout.design.theme.imageBackground
import cinescout.design.ui.CenteredProgress
import cinescout.design.ui.ErrorScreen
import cinescout.design.ui.ErrorText
import cinescout.design.util.NoContentDescription
import cinescout.lists.presentation.model.ItemsListState
import cinescout.lists.presentation.model.ListItemUiModel
import cinescout.lists.presentation.previewdata.ItemsListScreenPreviewDataProvider
import coil.compose.AsyncImage
import studio.forface.cinescout.design.R

@Composable
fun ItemsListScreen(state: ItemsListState, emptyListContent: @Composable () -> Unit, modifier: Modifier = Modifier) {
    Box(modifier = modifier.fillMaxSize()) {
        when (state) {
            is ItemsListState.Error -> ErrorScreen(text = state.message)
            ItemsListState.Loading -> CenteredProgress()
            is ItemsListState.Data -> ListContent(data = state, emptyListContent = emptyListContent)
        }
    }
}

@Composable
private fun ListContent(data: ItemsListState.Data, emptyListContent: @Composable () -> Unit) {
    when (data) {
        ItemsListState.Data.Empty ->     Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            emptyListContent()
        }
        is ItemsListState.Data.NotEmpty -> NotEmptyListContent(items = data.items)
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
private fun NotEmptyListContent(items: NonEmptyList<ListItemUiModel>) {
    LazyVerticalGrid(
        columns = GridCells.Adaptive(minSize = Dimens.Component.XXLarge),
        contentPadding = PaddingValues(horizontal = Dimens.Margin.XSmall)
    ) {
        items(items = items, key = { it.tmdbId.value }) { item ->
            ListItem(model = item, modifier = Modifier.animateItemPlacement())
        }
    }
}

@Composable
private fun ListItem(model: ListItemUiModel, modifier: Modifier = Modifier) {
    BoxWithConstraints(modifier = modifier.padding(Dimens.Margin.XSmall)) {
        ElevatedCard {
            Column {
                val imageWidth = this@BoxWithConstraints.maxWidth
                val imageHeight = imageWidth * 1.35f
                AsyncImage(
                    modifier = Modifier
                        .width(imageWidth)
                        .height(imageHeight)
                        .imageBackground(),
                    model = model.posterUrl,
                    contentScale = ContentScale.FillWidth,
                    contentDescription = NoContentDescription,
                    error = painterResource(id = R.drawable.ic_warning_30)
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

@Composable
@Preview(showBackground = true)
@Preview(showSystemUi = true, device = Devices.TABLET)
private fun ItemsListScreenPreview(
    @PreviewParameter(ItemsListScreenPreviewDataProvider::class) state: ItemsListState
) {
    CineScoutTheme {
        ItemsListScreen(state = state, emptyListContent = { ErrorText(text = TextRes("Empty List")) })
    }
}
