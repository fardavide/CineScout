package cinescout.model

import cinescout.error.NetworkError

sealed interface NetworkOperation {

    object Skipped : NetworkOperation
    data class Error(val error: NetworkError) : NetworkOperation
}
