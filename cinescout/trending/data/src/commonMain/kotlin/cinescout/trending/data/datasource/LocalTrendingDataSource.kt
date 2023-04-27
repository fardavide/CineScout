package cinescout.trending.data.datasource

import cinescout.screenplay.domain.model.ScreenplayIds
import cinescout.screenplay.domain.model.ScreenplayType
import kotlinx.coroutines.flow.Flow

interface LocalTrendingDataSource {

    fun findTrendingIds(type: ScreenplayType): Flow<List<ScreenplayIds>>

    suspend fun insertTrendingIds(ids: List<ScreenplayIds>)
}
