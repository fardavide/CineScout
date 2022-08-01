package cinescout.home.presentation.model

import cinescout.design.TextRes
import cinescout.design.util.Effect

data class HomeState(
    val accounts: Accounts,
    val appVersion: AppVersion,
    val loginEffect: Effect<Login>
) {

    data class Accounts(
        val primary: Account,
        val tmdb: Account,
        val trakt: Account
    ) {

        sealed interface Account {
            data class Data(val username: String, val imageUrl: String?) : Account
            data class Error(val message: TextRes) : Account
            object Loading : Account
            object NoAccountConnected : Account
        }
    }

    sealed interface AppVersion {
        data class Data(val version: Int) : AppVersion
        object Loading : AppVersion
    }

    sealed interface Login {
        data class Error(val message: TextRes) : Login
        object Linked : Login
        data class UserShouldAuthorizeApp(val authorizationUrl: String) : Login
    }

    companion object {

        val Loading = HomeState(
            accounts = Accounts(
                primary = Accounts.Account.Loading,
                tmdb = Accounts.Account.Loading,
                trakt = Accounts.Account.Loading
            ),
            appVersion = AppVersion.Loading,
            loginEffect = Effect.empty()
        )
    }
}