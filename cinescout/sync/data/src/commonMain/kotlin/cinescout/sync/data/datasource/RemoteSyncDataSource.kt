package cinescout.sync.data.datasource

import arrow.core.Either
import cinescout.model.NetworkOperation
import cinescout.sync.domain.model.LastActivities

interface RemoteSyncDataSource {

    suspend fun getLastActivities(): Either<NetworkOperation, LastActivities>
}
