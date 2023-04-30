package cinescout.trending.data.datasource

import arrow.core.Either
import cinescout.error.NetworkError
import cinescout.screenplay.domain.model.ScreenplayIds
import cinescout.screenplay.domain.model.ScreenplayTypeFilter

interface RemoteTrendingDataSource {

    suspend fun getTrendingIds(type: ScreenplayTypeFilter): Either<NetworkError, List<ScreenplayIds>>
}
