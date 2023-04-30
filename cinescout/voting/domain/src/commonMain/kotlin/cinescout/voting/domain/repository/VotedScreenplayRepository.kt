package cinescout.voting.domain.repository

import app.cash.paging.PagingSource
import cinescout.lists.domain.ListSorting
import cinescout.screenplay.domain.model.Screenplay
import cinescout.screenplay.domain.model.ScreenplayTypeFilter
import cinescout.screenplay.domain.model.TmdbScreenplayId
import kotlinx.coroutines.flow.Flow

interface VotedScreenplayRepository {

    fun getAllDisliked(type: ScreenplayTypeFilter): Flow<List<Screenplay>>

    fun getAllLiked(type: ScreenplayTypeFilter): Flow<List<Screenplay>>

    fun getPagedDisliked(sorting: ListSorting, type: ScreenplayTypeFilter): PagingSource<Int, Screenplay>

    fun getPagedLiked(sorting: ListSorting, type: ScreenplayTypeFilter): PagingSource<Int, Screenplay>

    suspend fun setDisliked(screenplayId: TmdbScreenplayId)

    suspend fun setLiked(screenplayId: TmdbScreenplayId)
}
