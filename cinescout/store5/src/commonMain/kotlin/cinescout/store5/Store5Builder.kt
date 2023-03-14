package cinescout.store5

import org.mobilenativefoundation.store.store5.Bookkeeper
import org.mobilenativefoundation.store.store5.Fetcher
import org.mobilenativefoundation.store.store5.MemoryPolicy
import org.mobilenativefoundation.store.store5.SourceOfTruth
import org.mobilenativefoundation.store.store5.StoreBuilder

class Store5Builder<Key : Any, Output : Any, Local : Any> internal constructor(
    private val builder: StoreBuilder<Key, Output, Output, Local>
) {

    fun cachePolicy(memoryPolicy: MemoryPolicy<Key, Output>) = apply { builder.cachePolicy(memoryPolicy) }

    fun build(): Store5<Key, Output> = RealStore5(builder.build())

    fun <Response : Any> build(
        updater: EitherUpdater<Key, Output, Response>,
        bookkeeper: Bookkeeper<Key>
    ): MutableStore5<Key, Output, Response> = RealMutableStore5(builder.build(updater, bookkeeper))

    fun <Response : Any> buildMutable(
        updater: EitherUpdater<Key, Output, Response>,
        bookkeeper: Bookkeeper<Key>
    ): MutableStore5<Key, Output, Response> = RealMutableStore5(builder.build(updater, bookkeeper))

    companion object {

        fun <Key : Any, Output : Any> from(fetcher: EitherFetcher<Key, Output>) =
            Store5Builder(StoreBuilder.from(fetcher))

        fun <Key : Any, Output : Any> from(
            fetcher: Fetcher<Key, Output>,
            sourceOfTruth: SourceOfTruth<Key, Output>
        ) = Store5Builder(StoreBuilder.from(fetcher, sourceOfTruth))
    }
}
