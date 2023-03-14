package cinescout.watchlist.domain

import cinescout.screenplay.domain.model.TmdbScreenplayId
import cinescout.store5.MutableStore5
import cinescout.watchlist.domain.model.WatchlistStoreKey

interface WatchlistIdsStore : MutableStore5<WatchlistStoreKey, List<TmdbScreenplayId>, Unit>
