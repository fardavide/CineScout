package cinescout.store5.ext

import arrow.core.right
import cinescout.store5.Store5ReadResponse
import org.mobilenativefoundation.store.store5.StoreReadResponseOrigin

fun <Output : Any> fetcherResponseDataOf(value: Output) = Store5ReadResponse.Data(
    value = value.right(),
    origin = StoreReadResponseOrigin.Fetcher
)
