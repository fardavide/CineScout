package entities.auth

import entities.Either
import entities.Error
import entities.field.EmailAddress
import entities.field.InvalidEmailError
import entities.field.InvalidPasswordError
import entities.field.Password

interface TmdbAuth {

    suspend fun login(email: EmailAddress, password: Password): Either<LoginError, Unit>

    sealed class LoginError : Error {
        object WrongCredentials : LoginError()
        sealed class InputError : LoginError() {
            data class InvalidEmail(val reason: InvalidEmailError) : InputError()
            data class InvalidPassword(val reason: InvalidPasswordError) : InputError()
        }
        data class NetworkError(val reason: entities.NetworkError) : LoginError()
    }
}
