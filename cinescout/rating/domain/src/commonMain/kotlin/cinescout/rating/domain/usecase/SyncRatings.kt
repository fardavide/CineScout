package cinescout.rating.domain.usecase

import arrow.core.Either
import cinescout.error.NetworkError
import cinescout.screenplay.domain.model.ScreenplayTypeFilter
import cinescout.sync.domain.model.RequiredSync

interface SyncRatings {

    suspend operator fun invoke(
        type: ScreenplayTypeFilter,
        requiredSync: RequiredSync
    ): Either<NetworkError, Unit>

    suspend operator fun invoke() = invoke(ScreenplayTypeFilter.All, RequiredSync.Complete)
}
