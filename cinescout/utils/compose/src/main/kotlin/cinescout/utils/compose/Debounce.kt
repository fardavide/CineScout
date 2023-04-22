package cinescout.utils.compose

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import cinescout.utils.compose.paging.PagingItemsState
import kotlinx.coroutines.delay
import kotlin.time.Duration
import kotlin.time.Duration.Companion.milliseconds

/**
 * Applies a debounce to a [PagingItemsState], to avoid blinking when the list is empty.
 */
@Composable
fun <I : Any, S : Any> debouncePagingItems(
    itemsState: PagingItemsState<I>,
    emptyDelay: Duration = 100.milliseconds,
    block: (PagingItemsState<I>) -> S
): S {
    var prevItemsState: PagingItemsState<I> by remember {
        mutableStateOf(PagingItemsState.Empty)
    }

    val finalItemsState = when (itemsState) {
        is PagingItemsState.Error, is PagingItemsState.NotEmpty -> {
            prevItemsState = itemsState
            itemsState
        }
        PagingItemsState.Empty -> {
            LaunchedEffect(itemsState) {
                delay(emptyDelay)
                prevItemsState = itemsState
            }
            prevItemsState
        }
        PagingItemsState.Loading -> {
            prevItemsState = when (val prev = prevItemsState) {
                PagingItemsState.Empty, is PagingItemsState.Error, PagingItemsState.Loading -> itemsState
                is PagingItemsState.NotEmpty -> prev.copy(isAlsoLoading = true)
            }
            prevItemsState
        }
    }
    return block(finalItemsState)
}
