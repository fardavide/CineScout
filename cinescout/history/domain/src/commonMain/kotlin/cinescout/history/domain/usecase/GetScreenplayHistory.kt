package cinescout.history.domain.usecase

import arrow.core.Either
import cinescout.error.NetworkError
import cinescout.history.domain.model.ScreenplayHistory
import cinescout.history.domain.model.ScreenplayHistoryStoreKey
import cinescout.history.domain.store.ScreenplayHistoryStore
import cinescout.screenplay.domain.model.ScreenplayIds
import cinescout.store5.ext.filterData
import kotlinx.coroutines.flow.Flow
import org.koin.core.annotation.Factory
import org.mobilenativefoundation.store.store5.StoreReadRequest

interface GetScreenplayHistory {

    operator fun invoke(
        screenplayId: ScreenplayIds,
        refresh: Boolean
    ): Flow<Either<NetworkError, ScreenplayHistory>>
}

@Factory
internal class RealGetScreenplayHistory(
    private val store: ScreenplayHistoryStore
) : GetScreenplayHistory {

    override operator fun invoke(
        screenplayId: ScreenplayIds,
        refresh: Boolean
    ): Flow<Either<NetworkError, ScreenplayHistory>> =
        store.stream(StoreReadRequest.cached(ScreenplayHistoryStoreKey.Read(screenplayId), refresh))
            .filterData()
}
