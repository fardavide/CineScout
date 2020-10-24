package auth.tmdb

import entities.Either
import entities.auth.TmdbAuth
import entities.field.EmailAddress
import entities.field.Password

class TmdbAuthImpl : TmdbAuth {

    override suspend fun login(email: EmailAddress, password: Password): Either<TmdbAuth.LoginError, Unit> {
        TODO("Not yet implemented")
    }

}
