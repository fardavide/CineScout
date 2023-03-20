package cinescout.utils.compose.paging

import androidx.paging.compose.LazyPagingItems
import cinescout.resources.TextRes
import cinescout.utils.compose.Effect

sealed interface PagingItemsState {

    object Empty : PagingItemsState

    data class Error(val message: TextRes) : PagingItemsState

    object Loading : PagingItemsState

    data class NotEmpty<T : Any>(
        val items: LazyPagingItems<T>,
        val error: Effect<TextRes>
    ) : PagingItemsState
}
