package cinescout.history.domain.usecase

import arrow.core.Either
import cinescout.error.NetworkError
import cinescout.history.domain.model.HistoryStoreKey
import cinescout.history.domain.model.ScreenplayHistory
import cinescout.history.domain.store.HistoryStore
import cinescout.screenplay.domain.model.ids.ScreenplayIds
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
    private val store: HistoryStore
) : GetScreenplayHistory {

    override operator fun invoke(
        screenplayId: ScreenplayIds,
        refresh: Boolean
    ): Flow<Either<NetworkError, ScreenplayHistory>> =
        store.stream(StoreReadRequest.cached(HistoryStoreKey.Read(screenplayId), refresh))
            .filterData()
}
