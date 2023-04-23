package cinescout.people.domain.usecase

import arrow.core.Either
import cinescout.error.NetworkError
import cinescout.people.domain.model.ScreenplayCredits
import cinescout.people.domain.store.ScreenplayCreditsStore
import cinescout.screenplay.domain.model.TmdbScreenplayId
import cinescout.store5.ext.filterData
import kotlinx.coroutines.flow.Flow
import org.koin.core.annotation.Factory
import org.mobilenativefoundation.store.store5.StoreReadRequest

@Factory
class GetScreenplayCredits(
    private val screenplayCreditsStore: ScreenplayCreditsStore

) {

    operator fun invoke(
        screenplayId: TmdbScreenplayId,
        refresh: Boolean
    ): Flow<Either<NetworkError, ScreenplayCredits>> = screenplayCreditsStore
        .stream(StoreReadRequest.cached(ScreenplayCreditsStore.Key(screenplayId), refresh))
        .filterData()
}
