package cinescout.store5

import arrow.core.Either
import cinescout.error.NetworkError
import org.mobilenativefoundation.store.store5.StoreReadResponseOrigin

sealed interface Store5ReadResponse<out Output : Any> {

    val origin: StoreReadResponseOrigin

    fun dataOrNull(): Either<NetworkError, Output>? = when (this) {
        is Data -> value
        else -> null
    }

    data class Data<out Output : Any>(
        val value: Either<NetworkError, Output>,
        override val origin: StoreReadResponseOrigin
    ) : Store5ReadResponse<Output>

    data class Loading(override val origin: StoreReadResponseOrigin) : Store5ReadResponse<Nothing>

    data class NoNewData(override val origin: StoreReadResponseOrigin) : Store5ReadResponse<Nothing>
}
