package cinescout.watchlist.domain.model

import cinescout.screenplay.domain.model.ScreenplayType
import cinescout.screenplay.domain.model.TmdbScreenplayId

sealed interface WatchlistStoreKey {

    data class Read(val type: ScreenplayType) : WatchlistStoreKey

    sealed interface Write : WatchlistStoreKey {

        data class Add(val id: TmdbScreenplayId) : Write

        data class Remove(val id: TmdbScreenplayId) : Write
    }
}
