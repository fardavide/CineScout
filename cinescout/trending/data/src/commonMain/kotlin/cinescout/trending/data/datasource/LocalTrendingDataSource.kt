package cinescout.trending.data.datasource

import cinescout.screenplay.domain.model.ScreenplayTypeFilter
import cinescout.screenplay.domain.model.ids.ScreenplayIds
import kotlinx.coroutines.flow.Flow

interface LocalTrendingDataSource {

    fun findTrendingIds(type: ScreenplayTypeFilter): Flow<List<ScreenplayIds>>

    suspend fun insertTrendingIds(ids: List<ScreenplayIds>)
}
