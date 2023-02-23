package cinescout.auth.tmdb.domain.usecase

import arrow.core.Either
import arrow.core.left
import cinescout.auth.domain.usecase.IsTmdbLinked
import cinescout.error.NetworkError
import cinescout.model.NetworkOperation
import cinescout.utils.kotlin.firstNotNull
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn
import org.koin.core.annotation.Single

@Single
class CallWithTmdbAccount(
    appScope: CoroutineScope,
    isTmdbLinked: IsTmdbLinked
) {

    @PublishedApi
    internal val isLinked = isTmdbLinked().stateIn(
        scope = appScope,
        started = SharingStarted.Eagerly,
        initialValue = null
    )

    suspend inline operator fun <T : Any> invoke(
        block: () -> Either<NetworkError, T>
    ): Either<NetworkOperation, T> = if (isLinked.value ?: isLinked.firstNotNull()) {
        block().mapLeft { networkError -> NetworkOperation.Error(networkError) }
    } else {
        NetworkOperation.Skipped.left()
    }
}
