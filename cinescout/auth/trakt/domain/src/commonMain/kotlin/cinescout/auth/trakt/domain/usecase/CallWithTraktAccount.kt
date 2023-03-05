package cinescout.auth.trakt.domain.usecase

import arrow.core.Either
import arrow.core.left
import arrow.core.right
import cinescout.auth.domain.usecase.IsTraktLinked
import cinescout.error.NetworkError
import cinescout.model.NetworkOperation
import cinescout.utils.kotlin.firstNotNull
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn
import org.koin.core.annotation.Single

interface CallWithTraktAccount {

    suspend operator fun <T : Any> invoke(
        block: suspend () -> Either<NetworkError, T>
    ): Either<NetworkOperation, T> = forResult(block)

    suspend fun forUnit(block: suspend () -> Either<NetworkError, Unit>): Either<NetworkError, Unit>

    suspend fun <T : Any> forResult(block: suspend () -> Either<NetworkError, T>): Either<NetworkOperation, T>
}

@Single
class RealCallWithTraktAccount(
    appScope: CoroutineScope,
    isTraktLinked: IsTraktLinked
) : CallWithTraktAccount {

    private val isLinked = isTraktLinked().stateIn(
        scope = appScope,
        started = SharingStarted.Eagerly,
        initialValue = null
    )

    override suspend fun forUnit(
        block: suspend () -> Either<NetworkError, Unit>
    ): Either<NetworkError, Unit> = if (isLinked.value ?: isLinked.firstNotNull()) {
        block()
    } else {
        Unit.right()
    }

    override suspend fun <T : Any> forResult(
        block: suspend () -> Either<NetworkError, T>
    ): Either<NetworkOperation, T> = if (isLinked.value ?: isLinked.firstNotNull()) {
        block().mapLeft { networkError -> NetworkOperation.Error(networkError) }
    } else {
        NetworkOperation.Skipped.left()
    }
}

class FakeCallWithTraktAccount(
    private val isLinked: Boolean
) : CallWithTraktAccount {
    
    override suspend fun forUnit(
        block: suspend () -> Either<NetworkError, Unit>
    ): Either<NetworkError, Unit> = when (isLinked) {
        true -> block()
        false -> Unit.right()
    }

    override suspend fun <T : Any> forResult(
        block: suspend () -> Either<NetworkError, T>
    ): Either<NetworkOperation, T> = when (isLinked) {
        true -> block().mapLeft { networkError -> NetworkOperation.Error(networkError) }
        false -> NetworkOperation.Skipped.left()
    }
}
