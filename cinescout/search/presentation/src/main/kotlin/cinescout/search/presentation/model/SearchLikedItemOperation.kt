package cinescout.search.presentation.model

import cinescout.screenplay.domain.model.TmdbScreenplayId

sealed interface SearchLikedItemOperation

sealed interface SearchLikeItemAction : SearchLikedItemOperation {

    data class LikeItem(val itemId: TmdbScreenplayId) : SearchLikeItemAction

    data class Search(val query: String) : SearchLikeItemAction

    class SelectItemType(val itemType: SearchLikedItemType) : SearchLikeItemAction
}

sealed interface SearchLikedItemEvent : SearchLikedItemOperation {

    data class QueryUpdated(val query: String) : SearchLikedItemEvent

    data class SearchResult(val result: SearchLikedItemState.SearchResult) : SearchLikedItemEvent
}
