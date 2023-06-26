package cinescout.popular.data.datasource

import arrow.core.Either
import cinescout.error.NetworkError
import cinescout.screenplay.domain.model.ScreenplayTypeFilter
import cinescout.screenplay.domain.model.id.ScreenplayIds

interface RemotePopularDataSource {

    suspend fun getPopularIds(type: ScreenplayTypeFilter): Either<NetworkError, List<ScreenplayIds>>
}
