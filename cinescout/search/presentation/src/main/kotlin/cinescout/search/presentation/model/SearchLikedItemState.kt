package cinescout.search.presentation.model

import arrow.core.NonEmptyList
import arrow.core.Option
import arrow.core.none
import arrow.core.some
import cinescout.design.TextRes

data class SearchLikedItemState(
    val query: String,
    val result: SearchResult
) {

    sealed interface SearchResult {

        fun items(): Option<NonEmptyList<SearchLikedItemUiModel>> = when (this) {
            is Data -> items.some()
            is Loading -> previousItems
            else -> none()
        }

        data class Data(val items: NonEmptyList<SearchLikedItemUiModel>) : SearchResult
        data class Error(val message: TextRes) : SearchResult
        object Idle : SearchResult
        data class Loading(val previousItems: Option<NonEmptyList<SearchLikedItemUiModel>>) : SearchResult
        object NoResults : SearchResult
    }

    companion object {

        val Idle = SearchLikedItemState(
            query = "",
            result = SearchResult.Idle
        )
    }
}
