package cinescout.voting.domain.repository

import app.cash.paging.PagingSource
import cinescout.lists.domain.ListParams
import cinescout.screenplay.domain.model.Screenplay
import cinescout.screenplay.domain.model.ScreenplayTypeFilter
import cinescout.screenplay.domain.model.id.ScreenplayIds
import kotlinx.coroutines.flow.Flow

interface VotedScreenplayRepository {

    fun getAllDisliked(type: ScreenplayTypeFilter): Flow<List<Screenplay>>

    fun getAllLiked(type: ScreenplayTypeFilter): Flow<List<Screenplay>>

    fun getPagedDisliked(params: ListParams): PagingSource<Int, Screenplay>

    fun getPagedLiked(params: ListParams): PagingSource<Int, Screenplay>

    suspend fun setDisliked(screenplayIds: ScreenplayIds)

    suspend fun setLiked(screenplayIds: ScreenplayIds)
}
