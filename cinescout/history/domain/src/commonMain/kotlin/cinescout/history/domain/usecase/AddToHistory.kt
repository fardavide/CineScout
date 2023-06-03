package cinescout.history.domain.usecase

import arrow.core.Either
import cinescout.error.NetworkError
import cinescout.history.domain.model.ScreenplayHistory
import cinescout.history.domain.model.ScreenplayHistoryStoreKey
import cinescout.history.domain.store.ScreenplayHistoryStore
import cinescout.screenplay.domain.model.ids.ScreenplayIds
import org.koin.core.annotation.Factory
import org.mobilenativefoundation.store.store5.StoreWriteRequest

interface AddToHistory {

    suspend operator fun invoke(screenplayIds: ScreenplayIds): Either<NetworkError, Unit>
}

@Factory
internal class RealAddToHistory(
    private val store: ScreenplayHistoryStore
) : AddToHistory {

    override suspend operator fun invoke(screenplayIds: ScreenplayIds): Either<NetworkError, Unit> =
        store.write(
            StoreWriteRequest.of(
                key = ScreenplayHistoryStoreKey.Write.Add(screenplayIds),
                value = ScreenplayHistory.empty(screenplayIds)
            )
        )
}
