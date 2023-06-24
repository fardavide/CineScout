package cinescout.sync.data.remote.datasource

import arrow.core.Either
import cinescout.auth.domain.usecase.CallWithTraktAccount
import cinescout.model.NetworkOperation
import cinescout.sync.data.datasource.RemoteSyncDataSource
import cinescout.sync.data.remote.mapper.TraktLastActivitiesMapper
import cinescout.sync.data.remote.service.LastActivitiesService
import cinescout.sync.domain.model.LastActivities
import org.koin.core.annotation.Factory

@Factory
internal class RealRemoteSyncDataSource(
    private val callWithTraktAccount: CallWithTraktAccount,
    private val mapper: TraktLastActivitiesMapper,
    private val service: LastActivitiesService
) : RemoteSyncDataSource {

    override suspend fun getLastActivities(): Either<NetworkOperation, LastActivities> =
        callWithTraktAccount {
            service.getLastActivities().map(mapper::toLastActivities)
        }
}
