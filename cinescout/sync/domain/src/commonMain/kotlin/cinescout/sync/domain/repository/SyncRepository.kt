package cinescout.sync.domain.repository

import arrow.core.Either
import cinescout.model.NetworkOperation
import cinescout.sync.domain.model.LastActivities

interface SyncRepository {

    suspend fun getLastActivities(): Either<NetworkOperation, LastActivities>
}
