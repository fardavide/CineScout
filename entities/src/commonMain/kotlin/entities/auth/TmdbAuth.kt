package entities.auth

import entities.Either
import entities.Error
import entities.auth.TmdbAuth.LoginError
import entities.auth.TmdbAuth.LoginState
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.Flow

interface TmdbAuth {

    fun login(): Flow<Either_LoginResult>

    sealed class LoginState {
        object Loading : LoginState()
        class ApproveRequestToken(
            val request: String,
            val resultChannel: Channel<Either<LoginError.TokenApprovalCancelled, Approved>>
        ) : LoginState() {
            object Approved
        }
        object Completed : LoginState()
    }

    sealed class LoginError : Error {
        object TokenApprovalCancelled : LoginError()
        data class NetworkError(val reason: entities.NetworkError) : LoginError()
    }
}

typealias Either_LoginResult = Either<LoginError, LoginState>
