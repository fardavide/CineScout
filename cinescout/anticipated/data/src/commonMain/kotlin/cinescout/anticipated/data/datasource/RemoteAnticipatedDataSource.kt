package cinescout.anticipated.data.datasource

import arrow.core.Either
import cinescout.error.NetworkError
import cinescout.screenplay.domain.model.ScreenplayTypeFilter
import cinescout.screenplay.domain.model.id.ScreenplayIds

interface RemoteAnticipatedDataSource {

    suspend fun getMostAnticipatedIds(type: ScreenplayTypeFilter): Either<NetworkError, List<ScreenplayIds>>
}
