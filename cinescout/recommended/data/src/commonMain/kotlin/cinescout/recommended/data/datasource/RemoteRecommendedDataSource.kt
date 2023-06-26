package cinescout.recommended.data.datasource

import arrow.core.Either
import cinescout.error.NetworkError
import cinescout.screenplay.domain.model.ScreenplayTypeFilter
import cinescout.screenplay.domain.model.id.ScreenplayIds

interface RemoteRecommendedDataSource {

    suspend fun getRecommendedIds(type: ScreenplayTypeFilter): Either<NetworkError, List<ScreenplayIds>>
}
