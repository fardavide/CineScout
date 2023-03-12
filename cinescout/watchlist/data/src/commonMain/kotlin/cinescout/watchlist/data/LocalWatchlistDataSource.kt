package cinescout.watchlist.data

import app.cash.paging.PagingSource
import cinescout.lists.domain.ListType
import cinescout.screenplay.domain.model.Screenplay
import cinescout.screenplay.domain.model.TmdbScreenplayId
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow

interface LocalWatchlistDataSource {

    fun findPagedWatchlist(type: ListType): PagingSource<Int, Screenplay>

    fun findWatchlistIds(type: ListType): Flow<List<TmdbScreenplayId>>

    suspend fun insertAllWatchlist(screenplays: List<Screenplay>)

    suspend fun insertAllWatchlistIds(ids: List<TmdbScreenplayId>)

    suspend fun deleteAllWatchlistIds()
}

class FakeLocalWatchlistDataSource : LocalWatchlistDataSource {

    private val mutableWatchlist: MutableStateFlow<List<Screenplay>> = MutableStateFlow(emptyList())
    val watchlist: Flow<List<Screenplay>> = mutableWatchlist

    override fun findPagedWatchlist(type: ListType): PagingSource<Int, Screenplay> {
        TODO("Not yet implemented")
    }

    override fun findWatchlistIds(type: ListType): Flow<List<TmdbScreenplayId>> {
        TODO("Not yet implemented")
    }

    override suspend fun insertAllWatchlist(screenplays: List<Screenplay>) {
        mutableWatchlist.emit((mutableWatchlist.value + screenplays).distinct())
    }

    override suspend fun insertAllWatchlistIds(ids: List<TmdbScreenplayId>) {
        TODO("Not yet implemented")
    }

    override suspend fun deleteAllWatchlistIds() {
        TODO("Not yet implemented")
    }
}
