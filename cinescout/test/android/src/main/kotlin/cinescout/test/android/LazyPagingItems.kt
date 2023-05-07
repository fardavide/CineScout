package cinescout.test.android

import androidx.paging.LoadState
import androidx.paging.LoadStates
import androidx.paging.PagingData
import androidx.paging.compose.LazyPagingItems
import io.kotest.core.test.TestScope
import io.kotest.core.test.testCoroutineScheduler
import io.mockk.mockk
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.setMain
import kotlin.reflect.full.memberFunctions
import kotlin.reflect.full.primaryConstructor
import kotlin.reflect.jvm.isAccessible

inline fun <reified T : Any> TestScope.createLazyPagingItems(
    isEmpty: Boolean,
    refresh: LoadState,
    dispatcher: CoroutineDispatcher = UnconfinedTestDispatcher(testCoroutineScheduler)
): LazyPagingItems<T> = createLazyPagingItems(
    data = if (isEmpty) emptyList() else listOf(mockk()),
    sourceLoadStates = createLoadStates(refresh = refresh),
    mediatorLoadStates = createLoadStates(refresh = refresh),
    dispatcher = dispatcher
)

fun <T : Any> createLazyPagingItems(
    data: List<T>,
    sourceLoadStates: LoadStates = createLoadStates(),
    mediatorLoadStates: LoadStates = createLoadStates(),
    dispatcher: CoroutineDispatcher
): LazyPagingItems<T> {
    val kClass = LazyPagingItems::class
    val constructor = checkNotNull(kClass.primaryConstructor) {
        "Primary constructor not found"
    }

    val pagingData = PagingData.from(
        data = data,
        sourceLoadStates = sourceLoadStates,
        mediatorLoadStates = mediatorLoadStates
    )

    @Suppress("UNCHECKED_CAST")
    val lazyPagingItems = constructor.call(flowOf(pagingData)) as LazyPagingItems<T>

    val updateItemSnapshotList =
        checkNotNull(kClass.memberFunctions.find { it.name == "updateItemSnapshotList" }) {
            "Function updateItemSnapshotList not found"
        }
    updateItemSnapshotList.apply {
        isAccessible = true
        Dispatchers.setMain(dispatcher)
        call(lazyPagingItems)
        isAccessible = false
    }

    return lazyPagingItems
}
