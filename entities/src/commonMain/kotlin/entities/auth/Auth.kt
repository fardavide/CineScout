package entities.auth

import entities.Either
import entities.Error
import entities.auth.Auth.LoginError
import entities.auth.Auth.LoginState
import kotlinx.coroutines.channels.Channel

interface Auth {

    sealed class LoginState {

        object Loading : LoginState()

        sealed class ApproveRequestToken<A: ApproveRequestToken.Approved> : LoginState() {
            abstract val request: String
            abstract val resultChannel: Channel<Either<LoginError.TokenApprovalCancelled, A>>

            data class WithCode(
                override val request: String,
                override val resultChannel: Channel<Either<LoginError.TokenApprovalCancelled, Approved.WithCode>>
            ) : ApproveRequestToken<Approved.WithCode>()

            data class WithoutCode(
                override val request: String,
                override val resultChannel: Channel<Either<LoginError.TokenApprovalCancelled, Approved.WithoutCode>>
            ) : ApproveRequestToken<Approved.WithoutCode>()

            sealed class Approved {
                object WithoutCode : Approved()
                data class WithCode(val code: String): Approved()
            }
        }
        object Completed : LoginState()
    }

    sealed class LoginError : Error {
        object TokenApprovalCancelled : LoginError()
        data class NetworkError(val reason: entities.NetworkError) : LoginError()
    }
}

typealias Either_LoginResult = Either<LoginError, LoginState>
