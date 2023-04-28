package cinescout.popular.data.datasource

import cinescout.screenplay.domain.model.ScreenplayIds
import cinescout.screenplay.domain.model.ScreenplayType
import kotlinx.coroutines.flow.Flow

interface LocalPopularDataSource {

    fun findPopularIds(type: ScreenplayType): Flow<List<ScreenplayIds>>

    suspend fun insertPopularIds(ids: List<ScreenplayIds>)
}
