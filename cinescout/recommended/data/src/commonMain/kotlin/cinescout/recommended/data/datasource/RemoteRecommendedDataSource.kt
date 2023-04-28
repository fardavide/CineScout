package cinescout.recommended.data.datasource

import arrow.core.Either
import cinescout.error.NetworkError
import cinescout.screenplay.domain.model.ScreenplayIds
import cinescout.screenplay.domain.model.ScreenplayType

interface RemoteRecommendedDataSource {

    suspend fun getRecommendedIds(type: ScreenplayType): Either<NetworkError, List<ScreenplayIds>>
}
