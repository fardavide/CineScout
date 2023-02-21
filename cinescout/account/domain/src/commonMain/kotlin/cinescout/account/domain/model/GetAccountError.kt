package cinescout.account.domain.model

import cinescout.error.NetworkError

sealed interface GetAccountError {

    data class Network(val networkError: NetworkError) : GetAccountError
    object NotConnected : GetAccountError
}
