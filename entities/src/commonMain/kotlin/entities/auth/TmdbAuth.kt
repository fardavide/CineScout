package entities.auth

import entities.Either
import entities.Error
import entities.NetworkError
import entities.field.InvalidEmailError
import entities.field.InvalidPasswordError
import kotlinx.coroutines.flow.Flow

interface TmdbAuth {

    fun login(): Flow<Either<NetworkError, LoginState>>

    sealed class LoginState {
        object Loading : LoginState()
        object RequestToken : LoginState()
        object Completed : LoginState()
    }

    sealed class LoginError : Error {
        object WrongCredentials : LoginError()
        sealed class InputError : LoginError() {
            data class InvalidEmail(val reason: InvalidEmailError) : InputError()
            data class InvalidPassword(val reason: InvalidPasswordError) : InputError()
        }
        data class NetworkError(val reason: entities.NetworkError) : LoginError()
    }
}
