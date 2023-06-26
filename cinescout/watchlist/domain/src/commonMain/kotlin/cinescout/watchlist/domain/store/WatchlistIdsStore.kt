package cinescout.watchlist.domain.store

import cinescout.screenplay.domain.model.id.ScreenplayIds
import cinescout.store5.MutableStore5
import cinescout.watchlist.domain.model.WatchlistStoreKey

interface WatchlistIdsStore : MutableStore5<WatchlistStoreKey, List<ScreenplayIds>, Unit>
