package cinescout.store

import arrow.core.Either
import cinescout.error.NetworkError

internal class ConsumableData<T> private constructor(private var value: Either<NetworkError, T>?) {

    fun consume(): Either<NetworkError, T>? = value
        .also { value = null }

    inline fun consume(block: (Either<NetworkError, T>) -> Unit) {
        consume()?.let(block)
    }

    companion object {

        fun <T> of(error: Either<NetworkError, T>) = ConsumableData(error)
    }
}
