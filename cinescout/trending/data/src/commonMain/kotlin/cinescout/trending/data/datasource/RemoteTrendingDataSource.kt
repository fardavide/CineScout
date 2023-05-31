package cinescout.trending.data.datasource

import arrow.core.Either
import cinescout.error.NetworkError
import cinescout.screenplay.domain.model.ScreenplayTypeFilter
import cinescout.screenplay.domain.model.ids.ScreenplayIds

interface RemoteTrendingDataSource {

    suspend fun getTrendingIds(type: ScreenplayTypeFilter): Either<NetworkError, List<ScreenplayIds>>
}
