package cinescout.store5

import org.mobilenativefoundation.store.store5.Bookkeeper

fun <Key : Any> Bookkeeper.Companion.empty() = by<Key>(
    clear = { false },
    clearAll = { false },
    getLastFailedSync = { null },
    setLastFailedSync = { _, _ -> false }
)
