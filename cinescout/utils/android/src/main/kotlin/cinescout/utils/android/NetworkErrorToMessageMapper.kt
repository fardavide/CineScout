package cinescout.utils.android

import cinescout.error.NetworkError
import cinescout.resources.R.string
import cinescout.resources.TextRes
import cinescout.resources.sample.MessageSample
import org.koin.core.annotation.Factory

@Factory
open class NetworkErrorToMessageMapper {

    open fun toMessage(networkError: NetworkError): TextRes {
        val resId = when (networkError) {
            NetworkError.BadRequest, NetworkError.Unknown -> string.network_error_unknown
            NetworkError.Forbidden -> string.network_error_forbidden
            NetworkError.Internal -> string.network_error_internal
            NetworkError.NoNetwork -> string.network_error_no_network
            NetworkError.NotFound -> string.network_error_not_found
            NetworkError.Unauthorized -> string.network_error_unauthorized
            NetworkError.Unreachable -> string.network_error_unreachable
        }
        return TextRes(resId)
    }
}

class FakeNetworkErrorToMessageMapper(
    private val message: TextRes = MessageSample.NoNetworkError
) : NetworkErrorToMessageMapper() {

    override fun toMessage(networkError: NetworkError): TextRes = message
}
