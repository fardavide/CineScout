package cinescout.anticipated.data.datasource

import cinescout.screenplay.domain.model.ScreenplayIds
import cinescout.screenplay.domain.model.ScreenplayType
import kotlinx.coroutines.flow.Flow

interface LocalAnticipatedDataSource {

    fun findMostAnticipatedIds(type: ScreenplayType): Flow<List<ScreenplayIds>>

    suspend fun insertMostAnticipatedIds(ids: List<ScreenplayIds>)
}
