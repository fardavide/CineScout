package cinescout.sync.domain.usecase

import arrow.core.Either
import arrow.core.left
import arrow.core.right
import cinescout.error.NetworkError
import cinescout.screenplay.domain.store.ScreenplayStore
import cinescout.sync.domain.repository.SyncRepository
import org.koin.core.annotation.Factory

interface FetchScreenplays {

    suspend operator fun invoke(): Either<NetworkError, Unit>
}

@Factory
internal class RealFetchScreenplays(
    private val repository: SyncRepository,
    private val screenplayStore: ScreenplayStore
) : FetchScreenplays {

    override suspend fun invoke(): Either<NetworkError, Unit> {
        repository.getAllNotFetchedScreenplayIds().map { id ->
            screenplayStore.getCached(id, refresh = false).onLeft { error ->
                return error.left()
            }
        }
        return Unit.right()
    }
}
