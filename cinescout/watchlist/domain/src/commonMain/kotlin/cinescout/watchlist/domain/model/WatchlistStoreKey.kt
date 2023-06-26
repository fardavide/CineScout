package cinescout.watchlist.domain.model

import cinescout.screenplay.domain.model.ScreenplayTypeFilter
import cinescout.screenplay.domain.model.id.ScreenplayIds
import cinescout.screenplay.domain.model.id.TmdbScreenplayId

sealed interface WatchlistStoreKey {

    data class Read(val type: ScreenplayTypeFilter) : WatchlistStoreKey

    sealed interface Write : WatchlistStoreKey {

        data class Add(val ids: ScreenplayIds) : Write

        data class Remove(val id: TmdbScreenplayId) : Write
    }
}
