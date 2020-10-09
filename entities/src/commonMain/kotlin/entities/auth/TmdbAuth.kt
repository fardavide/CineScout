package entities.auth

import entities.Either
import entities.EmailAddress
import entities.Error
import entities.NetworkError
import entities.NotBlankString
import entities.Validable

interface TmdbAuth {

    suspend fun login(email: EmailAddress, password: NotBlankString): Either<LoginError, Unit>

    sealed class LoginError : Error {
        object WrongCredentials : LoginError()
        class NetworkError(val reason: entities.NetworkError) : LoginError()
    }
}
