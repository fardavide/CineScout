package cinescout.history.domain.usecase

import arrow.core.Either
import cinescout.error.NetworkError
import cinescout.history.domain.model.ScreenplayHistory
import cinescout.screenplay.domain.model.ScreenplayIds
import kotlinx.coroutines.flow.Flow

interface GetScreenplayHistory {

    operator fun invoke(
        screenplayId: ScreenplayIds,
        refresh: Boolean
    ): Flow<Either<NetworkError, ScreenplayHistory>>
}
