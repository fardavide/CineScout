package cinescout.history.domain.usecase

import arrow.core.Either
import cinescout.error.NetworkError
import cinescout.screenplay.domain.model.ScreenplayTypeFilter
import cinescout.sync.domain.model.RequiredSync
import cinescout.sync.domain.model.SyncHistoryKey

interface SyncHistory {

    suspend operator fun invoke(key: SyncHistoryKey, requiredSync: RequiredSync): Either<NetworkError, Unit>

    suspend operator fun invoke() = invoke(
        key = SyncHistoryKey(ScreenplayTypeFilter.All),
        requiredSync = RequiredSync.Complete
    )
}
