package cinescout.utils.compose.paging

import androidx.paging.PagingData
import androidx.paging.compose.LazyPagingItems
import cinescout.CineScoutTestApi
import kotlinx.coroutines.flow.flowOf
import kotlin.reflect.full.primaryConstructor

/**
 * Creates a [LazyPagingItems] from the given [items].
 *  This uses reflection to access the internal constructor of [LazyPagingItems].
 */
@CineScoutTestApi
fun <T : Any> lazyPagingItemsOf(vararg items: T): LazyPagingItems<T> {
    val constructor = checkNotNull(LazyPagingItems::class.primaryConstructor)
    val arg = flowOf(PagingData.from(items.asList()))
    @Suppress("UNCHECKED_CAST")
    return constructor.call(arg) as LazyPagingItems<T>
}
