package cinescout.screenplay.domain.usecase

import arrow.core.Either
import cinescout.error.NetworkError
import cinescout.screenplay.domain.model.Screenplay
import cinescout.screenplay.domain.model.TmdbScreenplayId
import cinescout.screenplay.domain.store.ScreenplayStore
import cinescout.store5.ext.filterData
import kotlinx.coroutines.flow.Flow
import org.koin.core.annotation.Factory
import org.mobilenativefoundation.store.store5.StoreReadRequest

@Factory
class GetScreenplay(
    private val screenplayStore: ScreenplayStore
) {
    
    operator fun invoke(
        screenplayId: TmdbScreenplayId,
        refresh: Boolean
    ): Flow<Either<NetworkError, Screenplay>> =
        screenplayStore.stream(StoreReadRequest.cached(screenplayId, refresh)).filterData()
}
