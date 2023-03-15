package cinescout.screenplay.domain.model

import org.mobilenativefoundation.store.store5.MemoryPolicy

fun <Key : Any, Output : Any> ScreenplayMemoryPolicy(): MemoryPolicy<Key, Output> =
    MemoryPolicy.builder<Key, Output>()
        .setMaxSize(1_000)
        .build()
