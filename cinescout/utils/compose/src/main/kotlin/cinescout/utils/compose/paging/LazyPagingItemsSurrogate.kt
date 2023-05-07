package cinescout.utils.compose.paging

import androidx.paging.CombinedLoadStates
import androidx.paging.compose.LazyPagingItems

/**
 * A surrogate for [LazyPagingItems].
 * This is used to avoid mocking the [LazyPagingItems] class for testing.
 */
data class LazyPagingItemsSurrogate(
    val itemCount: Int,
    val loadState: CombinedLoadStates
)

fun LazyPagingItemsSurrogate(isEmpty: Boolean, loadStates: CombinedLoadStates) = LazyPagingItemsSurrogate(
    itemCount = if (isEmpty) 0 else 1,
    loadState = loadStates
)
