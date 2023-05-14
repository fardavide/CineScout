package cinescout.store5

import org.mobilenativefoundation.store.store5.Bookkeeper
import org.mobilenativefoundation.store.store5.Fetcher
import org.mobilenativefoundation.store.store5.MemoryPolicy
import org.mobilenativefoundation.store.store5.MutableStoreBuilder
import org.mobilenativefoundation.store.store5.SourceOfTruth

class MutableStore5Builder<Key : Any, Output : Any, Local : Any> internal constructor(
    private val builder: MutableStoreBuilder<Key, Output, Output, Local>
) {

    fun cachePolicy(memoryPolicy: MemoryPolicy<Key, Output>) = apply { builder.cachePolicy(memoryPolicy) }

    // fun build(): MutableStore5<Key, Output> = RealMutableStore5(builder.build())

    fun <Response : Any> build(
        updater: EitherUpdater<Key, Output, Response>,
        bookkeeper: Bookkeeper<Key>? = null
    ): MutableStore5<Key, Output, Response> = RealMutableStore5(builder.build(updater, bookkeeper))

    @Deprecated("Use build instead", ReplaceWith("build(updater, bookkeeper)"))
    fun <Response : Any> buildMutable(
        updater: EitherUpdater<Key, Output, Response>,
        bookkeeper: Bookkeeper<Key>? = null
    ): MutableStore5<Key, Output, Response> = RealMutableStore5(builder.build(updater, bookkeeper))

    companion object {

        // fun <Key : Any, Output : Any> from(fetcher: EitherFetcher<Key, Output>) =
        //     MutableStore5Builder(MutableStoreBuilder.from(fetcher))

        fun <Key : Any, Output : Any> from(
            fetcher: Fetcher<Key, Output>,
            sourceOfTruth: SourceOfTruth<Key, Output>
        ) = MutableStore5Builder(MutableStoreBuilder.from(fetcher, sourceOfTruth))
    }
}
