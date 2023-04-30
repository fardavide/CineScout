package cinescout.anticipated.data.datasource

import arrow.core.Either
import cinescout.error.NetworkError
import cinescout.screenplay.domain.model.ScreenplayIds
import cinescout.screenplay.domain.model.ScreenplayTypeFilter

interface RemoteAnticipatedDataSource {

    suspend fun getMostAnticipatedIds(type: ScreenplayTypeFilter): Either<NetworkError, List<ScreenplayIds>>
}
