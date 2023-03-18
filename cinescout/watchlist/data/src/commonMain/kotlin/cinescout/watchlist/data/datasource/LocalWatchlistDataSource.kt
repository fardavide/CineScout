package cinescout.watchlist.data.datasource

import app.cash.paging.PagingSource
import cinescout.screenplay.domain.model.Screenplay
import cinescout.screenplay.domain.model.ScreenplayType
import cinescout.screenplay.domain.model.TmdbScreenplayId
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow

interface LocalWatchlistDataSource {

    suspend fun delete(id: TmdbScreenplayId)

    suspend fun deleteAllWatchlistIds()

    fun findPagedWatchlist(type: ScreenplayType): PagingSource<Int, Screenplay>

    fun findWatchlistIds(type: ScreenplayType): Flow<List<TmdbScreenplayId>>

    suspend fun insert(id: TmdbScreenplayId)

    suspend fun insertAllWatchlist(screenplays: List<Screenplay>)

    suspend fun updateAllWatchlistIds(ids: List<TmdbScreenplayId>)
}

class FakeLocalWatchlistDataSource : LocalWatchlistDataSource {

    private val mutableWatchlist: MutableStateFlow<List<Screenplay>> = MutableStateFlow(emptyList())
    val watchlist: Flow<List<Screenplay>> = mutableWatchlist

    override fun findPagedWatchlist(type: ScreenplayType): PagingSource<Int, Screenplay> {
        TODO("Not yet implemented")
    }

    override fun findWatchlistIds(type: ScreenplayType): Flow<List<TmdbScreenplayId>> {
        TODO("Not yet implemented")
    }

    override suspend fun insert(id: TmdbScreenplayId) {
        TODO("Not yet implemented")
    }

    override suspend fun insertAllWatchlist(screenplays: List<Screenplay>) {
        mutableWatchlist.emit((mutableWatchlist.value + screenplays).distinct())
    }

    override suspend fun updateAllWatchlistIds(ids: List<TmdbScreenplayId>) {
        TODO("Not yet implemented")
    }

    override suspend fun delete(id: TmdbScreenplayId) {
        TODO("Not yet implemented")
    }

    override suspend fun deleteAllWatchlistIds() {
        TODO("Not yet implemented")
    }
}
