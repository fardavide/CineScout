package cinescout.lists.presentation.previewdata

import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import arrow.core.nonEmptyListOf
import cinescout.design.R.string
import cinescout.design.TextRes
import cinescout.lists.presentation.model.ListFilter
import cinescout.lists.presentation.model.ListType
import cinescout.lists.presentation.state.ItemsListState

object ItemsListScreenPreviewData {

    val AllEmptyList = ItemsListState(
        items = ItemsListState.ItemsState.Data.Empty,
        filter = ListFilter.Disliked,
        type = ListType.All
    )
    val Error = ItemsListState(
        items = ItemsListState.ItemsState.Error(TextRes(string.network_error_no_network)),
        filter = ListFilter.Disliked,
        type = ListType.All
    )
    val Loading = ItemsListState(
        items = ItemsListState.ItemsState.Loading,
        filter = ListFilter.Disliked,
        type = ListType.All
    )
    val MoviesEmptyList = ItemsListState(
        items = ItemsListState.ItemsState.Data.Empty,
        filter = ListFilter.Disliked,
        type = ListType.Movies
    )
    val NotEmptyList = ItemsListState(
        items = ItemsListState.ItemsState.Data.NotEmpty(
            nonEmptyListOf(
                ListItemUiModelPreviewData.Inception,
                ListItemUiModelPreviewData.TheWolfOfWallStreet
            )
        ),
        filter = ListFilter.Disliked,
        type = ListType.All
    )
    val TvShowsEmptyWatchlist = ItemsListState(
        items = ItemsListState.ItemsState.Data.Empty,
        filter = ListFilter.Disliked,
        type = ListType.TvShows
    )
}

class ItemsListScreenPreviewDataProvider : PreviewParameterProvider<ItemsListState> {

    override val values = sequenceOf(
        ItemsListScreenPreviewData.Loading,
        ItemsListScreenPreviewData.Error,
        ItemsListScreenPreviewData.AllEmptyList,
        ItemsListScreenPreviewData.MoviesEmptyList,
        ItemsListScreenPreviewData.TvShowsEmptyWatchlist,
        ItemsListScreenPreviewData.NotEmptyList
    )
}
