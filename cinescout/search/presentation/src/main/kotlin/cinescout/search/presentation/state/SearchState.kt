package cinescout.search.presentation.state

import cinescout.search.presentation.model.SearchItemUiModel
import cinescout.utils.compose.paging.PagingItemsState

internal data class SearchState(
    val query: String,
    val itemsState: PagingItemsState<SearchItemUiModel>
)
