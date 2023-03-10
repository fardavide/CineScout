package cinescout.store5.ext

import arrow.core.left
import arrow.core.right
import cinescout.error.NetworkError
import cinescout.store5.Store5ReadResponse
import org.mobilenativefoundation.store.store5.StoreReadResponseOrigin

fun <Output : Any> fetcherResponseDataOf(value: Output) = Store5ReadResponse.Data(
    value = value.right(),
    origin = StoreReadResponseOrigin.Fetcher
)

fun fetcherResponseDataOf(error: NetworkError) = Store5ReadResponse.Data(
    value = error.left(),
    origin = StoreReadResponseOrigin.Fetcher
)
