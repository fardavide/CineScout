package store

import arrow.core.Either
import cinescout.model.NetworkOperation

internal class ConsumableData<T> private constructor(private var value: Either<NetworkOperation, T>?) {

    fun consume(): Either<NetworkOperation, T>? = value
        .also { value = null }

    inline fun consume(block: (Either<NetworkOperation, T>) -> Unit) {
        consume()?.let(block)
    }

    companion object {

        fun <T> of(error: Either<NetworkOperation, T>) = ConsumableData(error)
    }
}
