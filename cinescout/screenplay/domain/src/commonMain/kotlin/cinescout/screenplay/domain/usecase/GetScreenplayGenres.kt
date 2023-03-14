package cinescout.screenplay.domain.usecase

import arrow.core.Either
import cinescout.error.NetworkError
import cinescout.screenplay.domain.model.ScreenplayGenres
import cinescout.screenplay.domain.model.TmdbScreenplayId
import cinescout.screenplay.domain.store.ScreenplayGenresStore
import cinescout.store5.ext.filterData
import kotlinx.coroutines.flow.Flow
import org.koin.core.annotation.Factory
import org.mobilenativefoundation.store.store5.StoreReadRequest

@Factory
class GetScreenplayGenres(
    private val screenplayGenresStore: ScreenplayGenresStore
) {
    
    operator fun invoke(
        screenplayId: TmdbScreenplayId,
        refresh: Boolean
    ): Flow<Either<NetworkError, ScreenplayGenres>> =
        screenplayGenresStore.stream(StoreReadRequest.cached(screenplayId, refresh)).filterData()
}
