package cinescout.watchlist.data.datasource

import app.cash.paging.PagingSource
import cinescout.CineScoutTestApi
import cinescout.lists.domain.ListParams
import cinescout.notImplementedFake
import cinescout.screenplay.domain.model.Screenplay
import cinescout.screenplay.domain.model.ScreenplayTypeFilter
import cinescout.screenplay.domain.model.ScreenplayWithGenreSlugs
import cinescout.screenplay.domain.model.id.ScreenplayIds
import cinescout.screenplay.domain.model.id.TmdbScreenplayId
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.update

interface LocalWatchlistDataSource {

    suspend fun delete(id: TmdbScreenplayId)

    fun findPagedWatchlist(params: ListParams): PagingSource<Int, Screenplay>

    fun findWatchlistIds(type: ScreenplayTypeFilter): Flow<List<ScreenplayIds>>

    suspend fun insert(ids: ScreenplayIds)

    suspend fun insertAllWatchlist(screenplays: List<ScreenplayWithGenreSlugs>)

    suspend fun updateAllWatchlist(screenplays: List<ScreenplayWithGenreSlugs>, type: ScreenplayTypeFilter)

    suspend fun updateAllWatchlistIds(ids: List<ScreenplayIds>, type: ScreenplayTypeFilter)
}

@CineScoutTestApi
class FakeLocalWatchlistDataSource : LocalWatchlistDataSource {

    private val mutableWatchlist: MutableStateFlow<List<Screenplay>> = MutableStateFlow(emptyList())
    val watchlist: Flow<List<Screenplay>> = mutableWatchlist

    override suspend fun delete(id: TmdbScreenplayId) {
        notImplementedFake()
    }

    override fun findPagedWatchlist(params: ListParams): PagingSource<Int, Screenplay> {
        notImplementedFake()
    }

    override fun findWatchlistIds(type: ScreenplayTypeFilter): Flow<List<ScreenplayIds>> {
        notImplementedFake()
    }

    override suspend fun insert(ids: ScreenplayIds) {
        notImplementedFake()
    }

    override suspend fun insertAllWatchlist(screenplays: List<ScreenplayWithGenreSlugs>) {
        mutableWatchlist.update { (it + screenplays.map(ScreenplayWithGenreSlugs::screenplay)).distinct() }
    }

    override suspend fun updateAllWatchlist(
        screenplays: List<ScreenplayWithGenreSlugs>,
        type: ScreenplayTypeFilter
    ) {
        notImplementedFake()
    }

    override suspend fun updateAllWatchlistIds(ids: List<ScreenplayIds>, type: ScreenplayTypeFilter) {
        notImplementedFake()
    }
}
