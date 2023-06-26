package cinescout.popular.data.datasource

import cinescout.screenplay.domain.model.ScreenplayTypeFilter
import cinescout.screenplay.domain.model.id.ScreenplayIds
import kotlinx.coroutines.flow.Flow

interface LocalPopularDataSource {

    fun findPopularIds(type: ScreenplayTypeFilter): Flow<List<ScreenplayIds>>

    suspend fun insertPopularIds(ids: List<ScreenplayIds>)
}
