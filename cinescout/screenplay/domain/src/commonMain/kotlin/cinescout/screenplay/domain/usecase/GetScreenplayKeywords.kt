package cinescout.screenplay.domain.usecase

import arrow.core.Either
import arrow.core.left
import arrow.core.right
import cinescout.error.NetworkError
import cinescout.screenplay.domain.model.ScreenplayKeywords
import cinescout.screenplay.domain.model.id.TmdbScreenplayId
import cinescout.screenplay.domain.store.ScreenplayKeywordsStore
import cinescout.store5.ext.filterData
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf
import org.jetbrains.annotations.VisibleForTesting
import org.koin.core.annotation.Factory
import org.mobilenativefoundation.store.store5.StoreReadRequest

interface GetScreenplayKeywords {

    operator fun invoke(
        screenplayId: TmdbScreenplayId,
        refresh: Boolean
    ): Flow<Either<NetworkError, ScreenplayKeywords>>
}


@Factory
class RealGetScreenplayKeywords(
    private val screenplayKeywordsStore: ScreenplayKeywordsStore
) : GetScreenplayKeywords {

    override operator fun invoke(
        screenplayId: TmdbScreenplayId,
        refresh: Boolean
    ): Flow<Either<NetworkError, ScreenplayKeywords>> =
        screenplayKeywordsStore.stream(StoreReadRequest.cached(screenplayId, refresh)).filterData()
}

@VisibleForTesting
class FakeGetScreenplayKeywords(
    private val screenplayKeywords: ScreenplayKeywords? = null
) : GetScreenplayKeywords {

    override operator fun invoke(
        screenplayId: TmdbScreenplayId,
        refresh: Boolean
    ): Flow<Either<NetworkError, ScreenplayKeywords>> =
        flowOf(screenplayKeywords?.right() ?: NetworkError.Unknown.left())
}
