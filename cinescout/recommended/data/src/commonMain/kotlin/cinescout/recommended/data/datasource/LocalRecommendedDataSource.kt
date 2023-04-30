package cinescout.recommended.data.datasource

import cinescout.screenplay.domain.model.ScreenplayIds
import cinescout.screenplay.domain.model.ScreenplayTypeFilter
import kotlinx.coroutines.flow.Flow

interface LocalRecommendedDataSource {

    fun findRecommendedIds(type: ScreenplayTypeFilter): Flow<List<ScreenplayIds>>

    suspend fun insertRecommendedIds(ids: List<ScreenplayIds>)
}
