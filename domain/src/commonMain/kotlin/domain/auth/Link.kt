package domain.auth

import domain.stats.SyncTmdbStats
import entities.Either
import entities.auth.Auth

interface Link {

    sealed class State {
        object None : Link.State()
        data class Login(val loginState: Auth.LoginState): Link.State()
        data class Sync(val syncState: SyncTmdbStats.State): Link.State()
    }

    sealed class Error {
        data class Login(val loginError: Auth.LoginError): Link.Error()
        data class Sync(val syncError: SyncTmdbStats.Error): Link.Error()
    }
}

typealias Either_LinkResult = Either<Link.Error, Link.State>
