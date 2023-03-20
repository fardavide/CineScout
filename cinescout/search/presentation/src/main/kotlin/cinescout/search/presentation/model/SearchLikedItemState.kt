package cinescout.search.presentation.model

import androidx.paging.compose.LazyPagingItems
import arrow.core.Option
import arrow.core.none
import arrow.core.some
import cinescout.resources.TextRes

data class SearchLikedItemState(
    val query: String,
    val items: LazyPagingItems<SearchLikedItemUiModel>
) {

    sealed interface SearchResult {

        fun items(): Option<LazyPagingItems<SearchLikedItemUiModel>> = when (this) {
            is Data -> items.some()
            is Loading -> previousItems
            else -> none()
        }

        data class Data(val items: LazyPagingItems<SearchLikedItemUiModel>) : SearchResult
        data class Error(val message: TextRes) : SearchResult
        object Idle : SearchResult
        data class Loading(val previousItems: Option<LazyPagingItems<SearchLikedItemUiModel>>) : SearchResult
        object NoResults : SearchResult
    }
}
