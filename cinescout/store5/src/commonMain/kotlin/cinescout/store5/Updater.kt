package cinescout.store5

import arrow.core.Either
import cinescout.error.NetworkError
import cinescout.model.NetworkOperation
import cinescout.model.handleSkippedAsRight
import org.mobilenativefoundation.store.store5.OnUpdaterCompletion
import org.mobilenativefoundation.store.store5.Updater
import org.mobilenativefoundation.store.store5.UpdaterResult

class EitherUpdater<Key : Any, Output : Any, Response : Any>(
    updater: Updater<Key, Output, Response>
) : Updater<Key, Output, Response> by updater {

    companion object {

        fun <Key : Any, Output : Any, Response : Any> by(
            post: suspend (Key, Output) -> Either<NetworkError, Response>,
            onCompletion: OnUpdaterCompletion<Response>? = null
        ): EitherUpdater<Key, Output, Response> {
            val block: suspend (Key, Output) -> UpdaterResult = { key: Key, output: Output ->
                post(key, output).fold(
                    ifLeft = { UpdaterResult.Error.Exception(UpdateException(it)) },
                    ifRight = { UpdaterResult.Success.Typed(it) }
                )
            }
            return EitherUpdater(Updater.by(post = block, onCompletion = onCompletion))
        }

        fun <Key : Any, Output : Any> byOperation(
            post: suspend (Key, Output) -> Either<NetworkOperation, Unit>,
            onCompletion: OnUpdaterCompletion<Unit>? = null
        ): EitherUpdater<Key, Output, Unit> {
            val block: suspend (Key, Output) -> UpdaterResult = { key: Key, output: Output ->
                post(key, output).handleSkippedAsRight().fold(
                    ifLeft = { UpdaterResult.Error.Exception(UpdateException(it)) },
                    ifRight = { UpdaterResult.Success.Typed(it) }
                )
            }
            return EitherUpdater(Updater.by(post = block, onCompletion = onCompletion))
        }
    }
}

class UpdateException(val error: NetworkError) : Exception()

