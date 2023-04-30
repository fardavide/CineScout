package cinescout.lists.presentation.previewdata

import cinescout.design.util.PreviewDataProvider
import cinescout.lists.domain.ListSorting
import cinescout.lists.presentation.model.ListFilter
import cinescout.lists.presentation.state.ItemsListState
import cinescout.resources.sample.MessageSample
import cinescout.screenplay.domain.model.ScreenplayTypeFilter
import cinescout.utils.compose.Effect
import cinescout.utils.compose.paging.PagingItemsState

object ItemsListScreenPreviewData {

    val AllEmptyList = ItemsListState(
        itemsState = PagingItemsState.Empty,
        filter = ListFilter.Disliked,
        scrollToTop = Effect.empty(),
        sorting = ListSorting.Rating.Descending,
        type = ScreenplayTypeFilter.All
    )
    val Error = ItemsListState(
        itemsState = PagingItemsState.Error(MessageSample.NoNetworkError),
        filter = ListFilter.Disliked,
        scrollToTop = Effect.empty(),
        sorting = ListSorting.Rating.Descending,
        type = ScreenplayTypeFilter.All
    )
    val Loading = ItemsListState(
        itemsState = PagingItemsState.Loading,
        filter = ListFilter.Disliked,
        scrollToTop = Effect.empty(),
        sorting = ListSorting.Rating.Descending,
        type = ScreenplayTypeFilter.All
    )
    val MoviesEmptyList = ItemsListState(
        itemsState = PagingItemsState.Empty,
        filter = ListFilter.Disliked,
        scrollToTop = Effect.empty(),
        sorting = ListSorting.Rating.Descending,
        type = ScreenplayTypeFilter.Movies
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
        scrollToTop = Effect.empty(),
        sorting = ListSorting.Rating.Descending,
        type = ScreenplayTypeFilter.All
    )
    val TvShowsEmptyWatchlist = ItemsListState(
        itemsState = PagingItemsState.Empty,
        filter = ListFilter.Disliked,
        scrollToTop = Effect.empty(),
        sorting = ListSorting.Rating.Descending,
        type = ScreenplayTypeFilter.TvShows
    )
}

internal class ItemsListScreenPreviewDataProvider : PreviewDataProvider<ItemsListState>(
    ItemsListScreenPreviewData.Loading,
    ItemsListScreenPreviewData.Error,
    ItemsListScreenPreviewData.AllEmptyList,
    ItemsListScreenPreviewData.MoviesEmptyList,
    ItemsListScreenPreviewData.TvShowsEmptyWatchlist,
    ItemsListScreenPreviewData.NotEmptyList
)
