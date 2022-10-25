package cinescout.search.presentation.reducer

import arrow.core.none
import arrow.core.some
import cinescout.search.presentation.model.SearchLikeItemAction
import cinescout.search.presentation.model.SearchLikedItemEvent
import cinescout.search.presentation.model.SearchLikedItemOperation
import cinescout.search.presentation.model.SearchLikedItemState
import cinescout.utils.android.Reducer

class SearchLikedItemReducer : Reducer<SearchLikedItemState, SearchLikedItemOperation> {

    override fun SearchLikedItemState.reduce(operation: SearchLikedItemOperation): SearchLikedItemState =
        when (operation) {
            is SearchLikeItemAction.LikeItem -> this
            is SearchLikeItemAction.Search -> this
            is SearchLikeItemAction.SelectItemType -> this
            is SearchLikedItemEvent.QueryUpdated -> onQueryUpdated(
                currentState = this,
                query = operation.query
            )
            is SearchLikedItemEvent.SearchResult -> onSearchResult(
                currentState = this,
                result = operation.result
            )
        }

    private fun onQueryUpdated(
        currentState: SearchLikedItemState,
        query: String
    ): SearchLikedItemState {
        val result = when {
            query.isBlank() -> SearchLikedItemState.SearchResult.Idle
            currentState.result is SearchLikedItemState.SearchResult.Data ->
                SearchLikedItemState.SearchResult.Loading(previousItems = currentState.result.items.some())
            else -> SearchLikedItemState.SearchResult.Loading(previousItems = none())
        }
        return currentState.copy(
            query = query,
            result = result
        )
    }

    private fun onSearchResult(
        currentState: SearchLikedItemState,
        result: SearchLikedItemState.SearchResult
    ): SearchLikedItemState = currentState.copy(result = result)
}
