package store

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf

sealed interface Reader<T : Any> {

    val flow: Flow<T?>
    
    data class FromSource<T : Any>(override val flow: Flow<T?>) : Reader<T>

    class Empty<T : Any> : Reader<T> {
        
        override val flow = flowOf(null)
    }

    companion object {

        fun <T : Any> fromSource(block: (() -> Flow<T?>)?): Reader<T> =
            if (block == null) Empty() else FromSource(block())
    }
}
