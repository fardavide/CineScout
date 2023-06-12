package cinescout.history.data.datasource

import cinescout.history.domain.model.ScreenplayHistory
import cinescout.screenplay.domain.model.SeasonAndEpisodeNumber
import cinescout.screenplay.domain.model.ids.MovieIds
import cinescout.screenplay.domain.model.ids.ScreenplayIds
import cinescout.screenplay.domain.model.ids.TvShowIds
import kotlinx.coroutines.flow.Flow

interface LocalScreenplayHistoryDataSource {

    suspend fun deleteAll(screenplayId: ScreenplayIds)
    fun find(screenplayIds: ScreenplayIds): Flow<ScreenplayHistory>
    suspend fun insertAll(history: ScreenplayHistory)
    suspend fun insertPlaceholder(movieIds: MovieIds)
    suspend fun insertPlaceholders(tvShowIds: TvShowIds, episodes: List<SeasonAndEpisodeNumber>)
}
