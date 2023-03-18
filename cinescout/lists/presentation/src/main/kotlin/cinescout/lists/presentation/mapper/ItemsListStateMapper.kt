package cinescout.lists.presentation.mapper

import androidx.paging.LoadState
import androidx.paging.compose.LazyPagingItems
import cinescout.design.NetworkErrorToMessageMapper
import cinescout.design.R.string
import cinescout.design.TextRes
import cinescout.design.util.Effect
import cinescout.lists.presentation.model.ListFilter
import cinescout.lists.presentation.model.ListItemUiModel
import cinescout.lists.presentation.state.ItemsListState
import cinescout.screenplay.domain.model.ScreenplayType
import cinescout.store5.FetchException
import cinescout.unsupported
import org.koin.core.annotation.Factory

@Factory
internal class ItemsListStateMapper(
    private val messageMapper: NetworkErrorToMessageMapper
) {

    fun toState(
        filter: ListFilter,
        items: LazyPagingItems<ListItemUiModel>,
        type: ScreenplayType
    ) = ItemsListState(
        errorMessage = toErrorMessage(items),
        filter = filter,
        itemsState = toItemsState(filter, items, type),
        type = type
    )

    private fun toItemsState(
        filter: ListFilter,
        items: LazyPagingItems<ListItemUiModel>,
        type: ScreenplayType
    ) = when (items.itemCount == 0) {
        false -> ItemsListState.ItemsState.NotEmpty(items)
        true -> when {
            items.loadState.refresh is LoadState.Loading ->
                ItemsListState.ItemsState.Loading
            items.loadState.refresh is LoadState.NotLoading ->
                ItemsListState.ItemsState.Empty(toEmptyMessage(filter, type))
            items.loadState.refresh is LoadState.Error ->
                ItemsListState.ItemsState.Error(toItemsError(items.loadState.refresh as LoadState.Error))
            items.loadState.prepend is LoadState.Error ->
                ItemsListState.ItemsState.Error(toItemsError(items.loadState.prepend as LoadState.Error))
            else -> unsupported
        }
    }

    private fun toEmptyMessage(filter: ListFilter, type: ScreenplayType): TextRes = when (type) {
        ScreenplayType.All -> when (filter) {
            ListFilter.Disliked -> string.lists_disliked_all_empty
            ListFilter.Liked -> string.lists_liked_all_empty
            ListFilter.Rated -> string.lists_rated_all_empty
            ListFilter.Watchlist -> string.lists_watchlist_all_empty
        }
        ScreenplayType.Movies -> when (filter) {
            ListFilter.Disliked -> string.lists_disliked_movies_empty
            ListFilter.Liked -> string.lists_liked_movies_empty
            ListFilter.Rated -> string.lists_rated_movies_empty
            ListFilter.Watchlist -> string.lists_watchlist_movies_empty
        }
        ScreenplayType.TvShows -> when (filter) {
            ListFilter.Disliked -> string.lists_disliked_tv_shows_empty
            ListFilter.Liked -> string.lists_liked_tv_shows_empty
            ListFilter.Rated -> string.lists_rated_tv_shows_empty
            ListFilter.Watchlist -> string.lists_watchlist_tv_shows_empty
        }
    }.let(TextRes::invoke)
    
    private fun toItemsError(loadState: LoadState.Error): TextRes = when (val error = loadState.error) {
        is FetchException -> messageMapper.toMessage(error.networkError)
        else -> error("Unknown error: $error")
    }

    private fun toErrorMessage(items: LazyPagingItems<ListItemUiModel>): Effect<TextRes> =
        when (items.itemCount) {
            0 -> Effect.empty()
            else -> when {
                items.loadState.refresh is LoadState.Error ->
                    Effect.of(toItemsError(items.loadState.refresh as LoadState.Error))
                items.loadState.prepend is LoadState.Error ->
                    Effect.of(toItemsError(items.loadState.prepend as LoadState.Error))
                else -> Effect.empty()
            }
        }
}
