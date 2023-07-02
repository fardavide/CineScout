package cinescout.history.data.datasource

import arrow.core.Either
import cinescout.history.domain.model.ScreenplayHistory
import cinescout.model.NetworkOperation
import cinescout.screenplay.domain.model.ScreenplayTypeFilter
import cinescout.screenplay.domain.model.id.ContentIds
import cinescout.screenplay.domain.model.id.ScreenplayIds

interface RemoteHistoryDataSource {

    suspend fun deleteHistory(screenplayId: ScreenplayIds): Either<NetworkOperation, Unit>
    suspend fun getAllHistories(type: ScreenplayTypeFilter): Either<NetworkOperation, List<ScreenplayHistory>>
    suspend fun getHistory(screenplayIds: ScreenplayIds): Either<NetworkOperation, ScreenplayHistory>
    suspend fun addToHistory(contentIds: ContentIds): Either<NetworkOperation, Unit>
}
