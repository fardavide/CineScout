package cinescout.auth.domain.usecase

import arrow.core.Either
import arrow.core.left
import arrow.core.right
import cinescout.error.NetworkError
import cinescout.model.NetworkOperation
import kotlinx.coroutines.flow.first
import org.koin.core.annotation.Factory

@Factory
class CallWithCurrentUser(
    @PublishedApi internal val isTmdbLinked: IsTmdbLinked,
    @PublishedApi internal val isTraktLinked: IsTraktLinked
) {

    @JvmName("invokeUnit")
    suspend inline operator fun invoke(
        tmdbCall: () -> Either<NetworkError, Unit>,
        traktCall: () -> Either<NetworkError, Unit>
    ): Either<NetworkError, Unit> {
        val (isTmdbLinked, isTraktLinked) = checkLinked()
        return when {
            isTmdbLinked -> tmdbCall()
            isTraktLinked -> traktCall()
            else -> Unit.right()
        }
    }

    @JvmName("invokeT")
    suspend inline operator fun <T : Any> invoke(
        tmdbCall: () -> Either<NetworkError, T>,
        traktCall: () -> Either<NetworkError, T>
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
