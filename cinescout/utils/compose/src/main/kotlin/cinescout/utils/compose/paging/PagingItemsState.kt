package cinescout.utils.compose.paging

import androidx.paging.compose.LazyPagingItems
import cinescout.resources.TextRes
import cinescout.utils.compose.Effect

sealed interface PagingItemsState<out T : Any> {

    val isLoading
        get() =
            this is Loading || this is NotEmpty && isAlsoLoading

    val errorMessage: Effect<TextRes>
        get() = when (this) {
            is NotEmpty -> error
            else -> Effect.empty()
        }

    object Empty : PagingItemsState<Nothing>

    data class Error(val message: TextRes) : PagingItemsState<Nothing>

    data object Loading : PagingItemsState<Nothing>

    data class NotEmpty<T : Any>(
        val items: LazyPagingItems<T>,
        val error: Effect<TextRes>,
        val isAlsoLoading: Boolean
    ) : PagingItemsState<T>
}
