package cinescout.popular.data.datasource

import arrow.core.Either
import cinescout.error.NetworkError
import cinescout.screenplay.domain.model.ScreenplayIds
import cinescout.screenplay.domain.model.ScreenplayType

interface RemotePopularDataSource {

    suspend fun getPopularIds(type: ScreenplayType): Either<NetworkError, List<ScreenplayIds>>
}