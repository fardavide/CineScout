package cinescout.history.data.datasource

import cinescout.history.domain.model.ScreenplayHistory
import cinescout.screenplay.domain.model.SeasonAndEpisodeNumber
import cinescout.screenplay.domain.model.ids.MovieIds
import cinescout.screenplay.domain.model.ids.ScreenplayIds
import cinescout.screenplay.domain.model.ids.TvShowIds
import kotlinx.coroutines.flow.Flow

/**
 * Since Trakt assigns an ID when a History item is added, we need to store a placeholder.
 * @see insertPlaceholder for Movie
 * @see insertPlaceholders for episodes of TvShow/Season
 */
interface LocalScreenplayHistoryDataSource {

    suspend fun deleteAll(screenplayId: ScreenplayIds)
    fun find(screenplayIds: ScreenplayIds): Flow<ScreenplayHistory>
    suspend fun insertAll(history: ScreenplayHistory)
    suspend fun insertPlaceholder(movieIds: MovieIds)
    suspend fun insertPlaceholders(tvShowIds: TvShowIds, episodes: List<SeasonAndEpisodeNumber>)
}
