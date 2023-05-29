package cinescout.history.data.datasource

import cinescout.history.domain.model.ScreenplayHistory
import cinescout.screenplay.domain.model.ScreenplayIds
import kotlinx.coroutines.flow.Flow

interface LocalScreenplayHistoryDataSource {

    suspend fun delete(screenplayId: ScreenplayIds)
    fun find(screenplayIds: ScreenplayIds): Flow<ScreenplayHistory?>
    suspend fun insert(history: ScreenplayHistory)
}
