package cinescout.recommended.data.datasource

import cinescout.screenplay.domain.model.ScreenplayTypeFilter
import cinescout.screenplay.domain.model.id.ScreenplayIds
import kotlinx.coroutines.flow.Flow

interface LocalRecommendedDataSource {

    fun findRecommendedIds(type: ScreenplayTypeFilter): Flow<List<ScreenplayIds>>

    suspend fun insertRecommendedIds(ids: List<ScreenplayIds>)
}
