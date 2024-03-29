package cinescout.anticipated.data.datasource

import cinescout.screenplay.domain.model.ScreenplayTypeFilter
import cinescout.screenplay.domain.model.id.ScreenplayIds
import kotlinx.coroutines.flow.Flow

interface LocalAnticipatedDataSource {

    fun findMostAnticipatedIds(type: ScreenplayTypeFilter): Flow<List<ScreenplayIds>>

    suspend fun insertMostAnticipatedIds(ids: List<ScreenplayIds>)
}
