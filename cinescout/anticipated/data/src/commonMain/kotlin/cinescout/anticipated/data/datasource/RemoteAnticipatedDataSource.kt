package cinescout.anticipated.data.datasource

import arrow.core.Either
import cinescout.error.NetworkError
import cinescout.screenplay.domain.model.ScreenplayIds
import cinescout.screenplay.domain.model.ScreenplayType

interface RemoteAnticipatedDataSource {

    suspend fun getMostAnticipatedIds(type: ScreenplayType): Either<NetworkError, List<ScreenplayIds>>
}
