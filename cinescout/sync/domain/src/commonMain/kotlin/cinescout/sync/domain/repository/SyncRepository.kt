package cinescout.sync.domain.repository

import arrow.core.Either
import cinescout.model.NetworkOperation
import cinescout.screenplay.domain.model.id.ScreenplayIds
import cinescout.sync.domain.model.LastActivities

interface SyncRepository {

    suspend fun getAllNotFetchedScreenplayIds(): List<ScreenplayIds>
    suspend fun getLastActivities(): Either<NetworkOperation, LastActivities>
}
