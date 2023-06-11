package cinescout.watchlist.data.datasource

import app.cash.paging.PagingSource
import cinescout.CineScoutTestApi
import cinescout.lists.domain.ListSorting
import cinescout.notImplementedFake
import cinescout.screenplay.domain.model.Screenplay
import cinescout.screenplay.domain.model.ScreenplayTypeFilter
import cinescout.screenplay.domain.model.ids.ScreenplayIds
import cinescout.screenplay.domain.model.ids.TmdbScreenplayId
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow

interface LocalWatchlistDataSource {

    suspend fun delete(id: TmdbScreenplayId)

    suspend fun deleteAllWatchlistIds()

    fun findPagedWatchlist(sorting: ListSorting, type: ScreenplayTypeFilter): PagingSource<Int, Screenplay>

    fun findWatchlistIds(type: ScreenplayTypeFilter): Flow<List<ScreenplayIds>>

    suspend fun insert(ids: ScreenplayIds)

    suspend fun insertAllWatchlist(screenplays: List<Screenplay>)

    suspend fun updateAllWatchlistIds(ids: List<ScreenplayIds>)
}

@CineScoutTestApi
class FakeLocalWatchlistDataSource : LocalWatchlistDataSource {

    private val mutableWatchlist: MutableStateFlow<List<Screenplay>> = MutableStateFlow(emptyList())
    val watchlist: Flow<List<Screenplay>> = mutableWatchlist

    override fun findPagedWatchlist(
        sorting: ListSorting,
        type: ScreenplayTypeFilter
    ): PagingSource<Int, Screenplay> {
        notImplementedFake()
    }

    override fun findWatchlistIds(type: ScreenplayTypeFilter): Flow<List<ScreenplayIds>> {
        notImplementedFake()
    }

    override suspend fun insert(ids: ScreenplayIds) {
        notImplementedFake()
    }

    override suspend fun insertAllWatchlist(screenplays: List<Screenplay>) {
        mutableWatchlist.emit((mutableWatchlist.value + screenplays).distinct())
    }

    override suspend fun updateAllWatchlistIds(ids: List<ScreenplayIds>) {
        notImplementedFake()
    }

    override suspend fun delete(id: TmdbScreenplayId) {
        notImplementedFake()
    }

    override suspend fun deleteAllWatchlistIds() {
        notImplementedFake()
    }
}
