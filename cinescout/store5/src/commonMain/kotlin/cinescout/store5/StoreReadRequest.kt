package cinescout.store5

import org.mobilenativefoundation.store.store5.StoreReadRequest

fun StoreReadRequest.Companion.cached(refresh: Boolean) = StoreReadRequest.cached(Unit, refresh)
