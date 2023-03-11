package cinescout.watchlist.domain

import cinescout.lists.domain.ListType
import cinescout.screenplay.domain.model.TmdbScreenplayId
import cinescout.store5.Store5

interface WatchlistIdsStore : Store5<WatchlistIdsStoreKey, List<TmdbScreenplayId>>

@JvmInline
value class WatchlistIdsStoreKey(val listType: ListType)
