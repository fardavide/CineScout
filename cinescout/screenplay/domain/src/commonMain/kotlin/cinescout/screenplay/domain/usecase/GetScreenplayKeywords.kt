package cinescout.screenplay.domain.usecase

import arrow.core.Either
import cinescout.error.NetworkError
import cinescout.screenplay.domain.model.ScreenplayKeywords
import cinescout.screenplay.domain.model.TmdbScreenplayId
import cinescout.screenplay.domain.store.ScreenplayKeywordsStore
import cinescout.store5.ext.filterData
import kotlinx.coroutines.flow.Flow
import org.koin.core.annotation.Factory
import org.mobilenativefoundation.store.store5.StoreReadRequest

@Factory
class GetScreenplayKeywords(
    private val screenplayKeywordsStore: ScreenplayKeywordsStore
) {
    
    operator fun invoke(
        screenplayId: TmdbScreenplayId,
        refresh: Boolean
    ): Flow<Either<NetworkError, ScreenplayKeywords>> =
        screenplayKeywordsStore.stream(StoreReadRequest.cached(screenplayId, refresh)).filterData()
}
