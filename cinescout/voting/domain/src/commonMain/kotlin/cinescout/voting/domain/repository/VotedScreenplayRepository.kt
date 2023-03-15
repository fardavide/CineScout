package cinescout.voting.domain.repository

import app.cash.paging.PagingSource
import cinescout.screenplay.domain.model.Screenplay
import cinescout.screenplay.domain.model.ScreenplayType
import cinescout.screenplay.domain.model.TmdbScreenplayId
import kotlinx.coroutines.flow.Flow

interface VotedScreenplayRepository {

    fun getAllDisliked(type: ScreenplayType): Flow<List<Screenplay>>

    fun getAllLiked(type: ScreenplayType): Flow<List<Screenplay>>

    fun getPagedDisliked(type: ScreenplayType): PagingSource<Int, Screenplay>

    fun getPagedLiked(type: ScreenplayType): PagingSource<Int, Screenplay>

    suspend fun setDisliked(screenplayId: TmdbScreenplayId)

    suspend fun setLiked(screenplayId: TmdbScreenplayId)
}
