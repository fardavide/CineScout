package cinescout.watchlist.data

import cinescout.lists.domain.ListType
import cinescout.screenplay.domain.model.TmdbScreenplayId
import kotlinx.coroutines.flow.Flow

interface LocalWatchlistDataSource {

    fun findWatchlistIds(type: ListType): Flow<List<TmdbScreenplayId>>

    suspend fun insertAllWatchlistIds(ids: List<TmdbScreenplayId>)

    suspend fun deleteAllWatchlistIds()
}
