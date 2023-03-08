package store

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf

sealed interface PagedReader<T : Any> {

    val flow: Flow<List<T>>
    
    data class FromSource<T : Any>(override val flow: Flow<List<T>>) : PagedReader<T>

    class Empty<T : Any> : PagedReader<T> {
        
        override val flow = flowOf(emptyList<T>())
    }

    companion object {

        fun <T : Any> fromSource(block: (() -> Flow<List<T>>)?): PagedReader<T> =
            if (block == null) Empty() else FromSource(block())

        fun <T : Any> fromSource(flow: Flow<List<T>>): PagedReader<T> = FromSource(flow)
    }
}
