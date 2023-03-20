package cinescout.utils.compose.paging

import androidx.paging.compose.LazyPagingItems
import cinescout.resources.TextRes
import cinescout.utils.compose.Effect

sealed interface PagingItemsState<out T : Any> {

    val errorMessage: Effect<TextRes>
        get() = when (this) {
            is NotEmpty -> error
            else -> Effect.empty()
        }

    object Empty : PagingItemsState<Nothing>

    data class Error(val message: TextRes) : PagingItemsState<Nothing>

    object Loading : PagingItemsState<Nothing>

    data class NotEmpty<T : Any>(
        val items: LazyPagingItems<T>,
        val error: Effect<TextRes>
    ) : PagingItemsState<T>
}
