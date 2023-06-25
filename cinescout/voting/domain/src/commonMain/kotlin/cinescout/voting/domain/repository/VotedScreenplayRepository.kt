package cinescout.voting.domain.repository

import app.cash.paging.PagingSource
import arrow.core.Option
import cinescout.lists.domain.ListSorting
import cinescout.screenplay.domain.model.Screenplay
import cinescout.screenplay.domain.model.ScreenplayTypeFilter
import cinescout.screenplay.domain.model.TmdbGenreId
import cinescout.screenplay.domain.model.ids.ScreenplayIds
import kotlinx.coroutines.flow.Flow

interface VotedScreenplayRepository {

    fun getAllDisliked(type: ScreenplayTypeFilter): Flow<List<Screenplay>>

    fun getAllLiked(type: ScreenplayTypeFilter): Flow<List<Screenplay>>

    fun getAllNotFetchedIds(): Flow<List<ScreenplayIds>>

    fun getPagedDisliked(
        genreFilter: Option<TmdbGenreId>,
        sorting: ListSorting,
        type: ScreenplayTypeFilter
    ): PagingSource<Int, Screenplay>

    fun getPagedLiked(
        genreFilter: Option<TmdbGenreId>,
        sorting: ListSorting,
        type: ScreenplayTypeFilter
    ): PagingSource<Int, Screenplay>

    suspend fun setDisliked(screenplayIds: ScreenplayIds)

    suspend fun setLiked(screenplayIds: ScreenplayIds)
}
