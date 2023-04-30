package cinescout.trending.data.datasource

import cinescout.screenplay.domain.model.ScreenplayIds
import cinescout.screenplay.domain.model.ScreenplayTypeFilter
import kotlinx.coroutines.flow.Flow

interface LocalTrendingDataSource {

    fun findTrendingIds(type: ScreenplayTypeFilter): Flow<List<ScreenplayIds>>

    suspend fun insertTrendingIds(ids: List<ScreenplayIds>)
}
