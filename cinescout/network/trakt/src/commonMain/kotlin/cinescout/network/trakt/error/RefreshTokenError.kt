package cinescout.network.trakt.error

import cinescout.error.NetworkError

sealed interface RefreshTokenError {
    data class Network(val error: NetworkError) : RefreshTokenError

    object NoRefreshToken : RefreshTokenError
}
