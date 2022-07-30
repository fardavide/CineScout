package cinescout.account.tmdb.domain.model

import cinescout.error.NetworkError

sealed interface GetAccountError {

    data class Network(val networkError: NetworkError) : GetAccountError
    object NoAccountConnected : GetAccountError
}
