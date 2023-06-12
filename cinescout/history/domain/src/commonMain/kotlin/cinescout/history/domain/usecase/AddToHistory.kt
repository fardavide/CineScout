package cinescout.history.domain.usecase

import arrow.core.Either
import arrow.core.right
import cinescout.CineScoutTestApi
import cinescout.error.NetworkError
import cinescout.history.domain.model.HistoryStoreKey
import cinescout.history.domain.model.ScreenplayHistory
import cinescout.history.domain.store.HistoryStore
import cinescout.screenplay.domain.model.ids.ScreenplayIds
import org.koin.core.annotation.Factory
import org.mobilenativefoundation.store.store5.StoreWriteRequest

interface AddToHistory {

    suspend operator fun invoke(screenplayIds: ScreenplayIds): Either<NetworkError, Unit>
}

@Factory
internal class RealAddToHistory(
    private val store: HistoryStore
) : AddToHistory {

    override suspend operator fun invoke(screenplayIds: ScreenplayIds): Either<NetworkError, Unit> =
        store.write(
            StoreWriteRequest.of(
                key = HistoryStoreKey.Write.Add(screenplayIds),
                value = ScreenplayHistory.empty(screenplayIds)
            )
        )
}

@CineScoutTestApi
class FakeAddToHistory : AddToHistory {

    override suspend operator fun invoke(screenplayIds: ScreenplayIds): Either<NetworkError, Unit> =
        Unit.right()
}
