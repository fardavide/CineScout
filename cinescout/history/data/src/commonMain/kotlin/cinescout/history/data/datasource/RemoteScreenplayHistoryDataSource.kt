package cinescout.history.data.datasource

import arrow.core.Either
import cinescout.history.domain.model.ScreenplayHistory
import cinescout.model.NetworkOperation
import cinescout.screenplay.domain.model.ScreenplayIds

interface RemoteScreenplayHistoryDataSource {

    suspend fun deleteHistory(screenplayId: ScreenplayIds): Either<NetworkOperation, Unit>
    suspend fun getHistory(screenplayIds: ScreenplayIds): Either<NetworkOperation, ScreenplayHistory>
    suspend fun postAddToHistory(screenplayIds: ScreenplayIds): Either<NetworkOperation, Unit>
}
