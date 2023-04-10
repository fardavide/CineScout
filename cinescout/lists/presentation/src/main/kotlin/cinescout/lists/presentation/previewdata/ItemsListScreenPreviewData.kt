package cinescout.lists.presentation.previewdata

import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import cinescout.lists.domain.ListSorting
import cinescout.lists.presentation.model.ListFilter
import cinescout.lists.presentation.state.ItemsListState
import cinescout.resources.sample.MessageSample
import cinescout.screenplay.domain.model.ScreenplayType
import cinescout.utils.compose.paging.PagingItemsState

object ItemsListScreenPreviewData {

    val AllEmptyList = ItemsListState(
        itemsState = PagingItemsState.Empty,
        filter = ListFilter.Disliked,
        sorting = ListSorting.Rating.Descending,
        type = ScreenplayType.All
    )
    val Error = ItemsListState(
        itemsState = PagingItemsState.Error(MessageSample.NoNetworkError),
        filter = ListFilter.Disliked,
        sorting = ListSorting.Rating.Descending,
        type = ScreenplayType.All
    )
    val Loading = ItemsListState(
        itemsState = PagingItemsState.Loading,
        filter = ListFilter.Disliked,
        sorting = ListSorting.Rating.Descending,
        type = ScreenplayType.All
    )
    val MoviesEmptyList = ItemsListState(
        itemsState = PagingItemsState.Empty,
        filter = ListFilter.Disliked,
        sorting = ListSorting.Rating.Descending,
        type = ScreenplayType.Movies
    )
    val NotEmptyList = ItemsListState(
        itemsState = TODO(),
//        items = ItemsListState.ItemsState.Data.NotEmpty(
//            nonEmptyListOf(
//                ListItemUiModelSample.Inception,
//                ListItemUiModelSample.TheWolfOfWallStreet
//            )
//        ),
        filter = ListFilter.Disliked,
        sorting = ListSorting.Rating.Descending,
        type = ScreenplayType.All
    )
    val TvShowsEmptyWatchlist = ItemsListState(
        itemsState = PagingItemsState.Empty,
        filter = ListFilter.Disliked,
        sorting = ListSorting.Rating.Descending,
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
