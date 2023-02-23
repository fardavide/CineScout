package cinescout.auth.domain.usecase

import arrow.core.Either
import arrow.core.left
import arrow.core.right
import cinescout.error.NetworkError
import cinescout.model.NetworkOperation
import kotlinx.coroutines.flow.first
import org.koin.core.annotation.Factory

interface CallWithCurrentUser {

    suspend fun forUnit(
        tmdbCall: suspend () -> Either<NetworkError, Unit>,
        traktCall: suspend () -> Either<NetworkError, Unit>
    ): Either<NetworkError, Unit>

    suspend fun <T : Any> forResult(
        tmdbCall: suspend () -> Either<NetworkError, T>,
        traktCall: suspend () -> Either<NetworkError, T>
    ): Either<NetworkOperation, T>

    companion object {

        const val BothLinkedErrorMessage = "Both TMDB and Trakt are linked. This is not supported."
    }
}

@Factory
class RealCallWithCurrentUser(
    @PublishedApi internal val isTmdbLinked: IsTmdbLinked,
    @PublishedApi internal val isTraktLinked: IsTraktLinked
) : CallWithCurrentUser {

    override suspend fun forUnit(
        tmdbCall: suspend () -> Either<NetworkError, Unit>,
        traktCall: suspend () -> Either<NetworkError, Unit>
    ): Either<NetworkError, Unit> {
        val (isTmdbLinked, isTraktLinked) = checkLinked()
        return when {
            isTmdbLinked -> tmdbCall()
            isTraktLinked -> traktCall()
            else -> Unit.right()
        }
    }

    override suspend fun <T : Any> forResult(
        tmdbCall: suspend () -> Either<NetworkError, T>,
        traktCall: suspend () -> Either<NetworkError, T>
    ): Either<NetworkOperation, T> {
        val (isTmdbLinked, isTraktLinked) = checkLinked()
        return when {
            isTmdbLinked -> tmdbCall().mapLeft(NetworkOperation::Error)
            isTraktLinked -> traktCall().mapLeft(NetworkOperation::Error)
            else -> NetworkOperation.Skipped.left()
        }
    }

    @PublishedApi
    internal suspend fun checkLinked(): Pair<Boolean, Boolean> {
        val isTmdbLinked = isTmdbLinked().first()
        val isTraktLinked = isTraktLinked().first()
        check(isTmdbLinked.not() || isTraktLinked.not(), ::BothLinkedErrorMessage)
        return isTmdbLinked to isTraktLinked
    }

    companion object {

        internal const val BothLinkedErrorMessage = "Both TMDB and Trakt are linked. This is not supported."
    }
}

class FakeCallWithCurrentUser(
    private val isTmdbLinked: Boolean,
    private val isTraktLinked: Boolean
) : CallWithCurrentUser {

    override suspend fun forUnit(
        tmdbCall: suspend () -> Either<NetworkError, Unit>,
        traktCall: suspend () -> Either<NetworkError, Unit>
    ): Either<NetworkError, Unit> = when {
        isTmdbLinked -> tmdbCall()
        isTraktLinked -> traktCall()
        else -> Unit.right()
    }

    override suspend fun <T : Any> forResult(
        tmdbCall: suspend () -> Either<NetworkError, T>,
        traktCall: suspend () -> Either<NetworkError, T>
    ): Either<NetworkOperation, T> = when {
        isTmdbLinked -> tmdbCall().mapLeft(NetworkOperation::Error)
        isTraktLinked -> traktCall().mapLeft(NetworkOperation::Error)
        else -> NetworkOperation.Skipped.left()
    }
}
