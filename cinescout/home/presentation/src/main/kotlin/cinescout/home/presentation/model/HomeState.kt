package cinescout.home.presentation.model

import cinescout.account.tmdb.domain.model.TmdbAccount
import cinescout.design.TextRes
import cinescout.design.util.Effect

data class HomeState(
    val account: Account,
    val loginEffect: Effect<Login>
) {

    sealed interface Account {
        data class Data(val account: TmdbAccount) : Account
        data class Error(val message: TextRes) : Account
        object Loading : Account
        object NoAccountConnected : Account
    }

    sealed interface Login {
        data class Error(val message: TextRes) : Login
        object Linked : Login
        data class UserShouldAuthorizeApp(val authorizationUrl: String) : Login
    }

    companion object {

        val Idle = HomeState(
            account = Account.Loading,
            loginEffect = Effect.empty()
        )
    }
}
