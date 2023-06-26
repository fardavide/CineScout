package cinescout.screenplay.domain.usecase

import arrow.core.Either
import arrow.core.left
import arrow.core.right
import cinescout.error.NetworkError
import cinescout.screenplay.domain.model.ScreenplayGenres
import cinescout.screenplay.domain.model.id.ScreenplayIds
import cinescout.screenplay.domain.store.ScreenplayGenresStore
import cinescout.store5.ext.filterData
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf
import org.jetbrains.annotations.VisibleForTesting
import org.koin.core.annotation.Factory
import org.mobilenativefoundation.store.store5.StoreReadRequest

interface GetScreenplayGenres {

    operator fun invoke(
        screenplayIds: ScreenplayIds,
        refresh: Boolean
    ): Flow<Either<NetworkError, ScreenplayGenres>>
}

@Factory
class RealGetScreenplayGenres(
    private val screenplayGenresStore: ScreenplayGenresStore
) : GetScreenplayGenres {

    override operator fun invoke(
        screenplayIds: ScreenplayIds,
        refresh: Boolean
    ): Flow<Either<NetworkError, ScreenplayGenres>> =
        screenplayGenresStore.stream(StoreReadRequest.cached(screenplayIds, refresh)).filterData()
}

@VisibleForTesting
class FakeGetScreenplayGenres(
    private val genres: ScreenplayGenres? = null
) : GetScreenplayGenres {

    override operator fun invoke(
        screenplayIds: ScreenplayIds,
        refresh: Boolean
    ): Flow<Either<NetworkError, ScreenplayGenres>> = flowOf(genres?.right() ?: NetworkError.Unknown.left())
}
