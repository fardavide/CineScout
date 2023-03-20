package cinescout.test.android

import androidx.paging.LoadState
import androidx.paging.compose.LazyPagingItems
import io.mockk.every
import io.mockk.mockk

fun <T : Any> mockLazyPagingItems(isEmpty: Boolean, refresh: LoadState): LazyPagingItems<T> = mockk {
    every { loadState } returns mockk loadState@{
        every { this@loadState.refresh } returns refresh
        every { this@loadState.append } returns LoadState.Loading
        every { this@loadState.prepend } returns LoadState.Loading
    }
    every { itemCount } returns if (isEmpty) 0 else 1
}
