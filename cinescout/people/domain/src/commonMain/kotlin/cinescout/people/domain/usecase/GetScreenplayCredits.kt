package cinescout.people.domain.usecase

import arrow.core.Either
import arrow.core.left
import arrow.core.right
import cinescout.error.NetworkError
import cinescout.people.domain.model.ScreenplayCredits
import cinescout.people.domain.store.ScreenplayCreditsStore
import cinescout.screenplay.domain.model.TmdbScreenplayId
import cinescout.store5.ext.filterData
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf
import org.jetbrains.annotations.VisibleForTesting
import org.koin.core.annotation.Factory
import org.mobilenativefoundation.store.store5.StoreReadRequest

interface GetScreenplayCredits {

    operator fun invoke(
        screenplayId: TmdbScreenplayId,
        refresh: Boolean
    ): Flow<Either<NetworkError, ScreenplayCredits>>
}

@Factory
internal class RealGetScreenplayCredits(
    private val screenplayCreditsStore: ScreenplayCreditsStore
) : GetScreenplayCredits {

    override operator fun invoke(
        screenplayId: TmdbScreenplayId,
        refresh: Boolean
    ): Flow<Either<NetworkError, ScreenplayCredits>> = screenplayCreditsStore
        .stream(StoreReadRequest.cached(ScreenplayCreditsStore.Key(screenplayId), refresh))
        .filterData()
}

@VisibleForTesting
class FakeGetScreenplayCredits(
    private val credits: ScreenplayCredits? = null
) : GetScreenplayCredits {

    override operator fun invoke(
        screenplayId: TmdbScreenplayId,
        refresh: Boolean
    ): Flow<Either<NetworkError, ScreenplayCredits>> = flowOf(credits?.right() ?: NetworkError.Unknown.left())
}
