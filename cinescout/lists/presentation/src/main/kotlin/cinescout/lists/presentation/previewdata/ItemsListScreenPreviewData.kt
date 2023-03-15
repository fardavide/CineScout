package cinescout.lists.presentation.previewdata

import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import cinescout.lists.presentation.model.ListFilter
import cinescout.lists.presentation.state.ItemsListState
import cinescout.screenplay.domain.model.ScreenplayType

object ItemsListScreenPreviewData {

    val AllEmptyList = ItemsListState(
        items = TODO(), // ItemsListState.ItemsState.Data.Empty(TextRes("No items in your list")),
        filter = ListFilter.Disliked,
        type = ScreenplayType.All
    )
    val Error = ItemsListState(
        items = TODO(), // ItemsListState.ItemsState.Error(TextRes(string.network_error_no_network)),
        filter = ListFilter.Disliked,
        type = ScreenplayType.All
    )
    val Loading = ItemsListState(
        items = TODO(), // ItemsListState.ItemsState.Loading,
        filter = ListFilter.Disliked,
        type = ScreenplayType.All
    )
    val MoviesEmptyList = ItemsListState(
        items = TODO(), // ItemsListState.ItemsState.Data.Empty(TextRes("No items in your list")),
        filter = ListFilter.Disliked,
        type = ScreenplayType.Movies
    )
    val NotEmptyList = ItemsListState(
        items = TODO(),
//        items = ItemsListState.ItemsState.Data.NotEmpty(
//            nonEmptyListOf(
//                ListItemUiModelSample.Inception,
//                ListItemUiModelSample.TheWolfOfWallStreet
//            )
//        ),
        filter = ListFilter.Disliked,
        type = ScreenplayType.All
    )
    val TvShowsEmptyWatchlist = ItemsListState(
        items = TODO(), // ItemsListState.ItemsState.Data.Empty(TextRes("No items in your list")),
        filter = ListFilter.Disliked,
        type = ScreenplayType.TvShows
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
