package cinescout.search.presentation.state

import cinescout.search.presentation.model.SearchLikedItemUiModel
import cinescout.utils.compose.paging.PagingItemsState

data class SearchLikedItemState(
    val query: String,
    val itemsState: PagingItemsState<SearchLikedItemUiModel>
)
