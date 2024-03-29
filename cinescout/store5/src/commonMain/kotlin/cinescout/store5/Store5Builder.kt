package cinescout.store5

import org.mobilenativefoundation.store.store5.Fetcher
import org.mobilenativefoundation.store.store5.MemoryPolicy
import org.mobilenativefoundation.store.store5.SourceOfTruth
import org.mobilenativefoundation.store.store5.StoreBuilder

class Store5Builder<Key : Any, Output : Any> internal constructor(
    private val builder: StoreBuilder<Key, Output>
) {

    fun cachePolicy(memoryPolicy: MemoryPolicy<Key, Output>) = apply { builder.cachePolicy(memoryPolicy) }

    fun build(): Store5<Key, Output> = RealStore5(builder.build())

    companion object {

        fun <Key : Any, Output : Any> from(fetcher: EitherFetcher<Key, Output>): Store5Builder<Key, Output> =
            Store5Builder(StoreBuilder.from(fetcher))

        fun <Key : Any, Output : Any> from(
            fetcher: Fetcher<Key, Output>,
            sourceOfTruth: SourceOfTruth<Key, Output, Output>
        ): Store5Builder<Key, Output> = Store5Builder(StoreBuilder.from(fetcher, sourceOfTruth))
    }
}
