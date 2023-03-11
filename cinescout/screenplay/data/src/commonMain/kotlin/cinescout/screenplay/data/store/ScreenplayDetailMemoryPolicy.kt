package cinescout.screenplay.data.store

import org.mobilenativefoundation.store.store5.MemoryPolicy

fun <Key : Any, Output : Any> ScreenplayDetailMemoryPolicy(): MemoryPolicy<Key, Output> =
    MemoryPolicy.builder<Key, Output>()
        .setMaxSize(1_000)
        .build()

