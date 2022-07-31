package cinescout.home.presentation.model

import cinescout.design.TextRes
import cinescout.design.util.Effect

data class HomeState(
    val accounts: Accounts,
    val appVersion: AppVersion,
    val loginEffect: Effect<Login>
) {

    constructor(
        account: Accounts.Account,
        appVersion: AppVersion,
        loginEffect: Effect<Login>
    ) : this(
        Accounts(account, account),
        appVersion,
        loginEffect
    )

    data class Accounts(
        val primary: Account,
        val tmdb: Account
    ) {

        sealed interface Account {
            data class Data(val username: String) : Account
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
                tmdb = Accounts.Account.Loading
            ),
            appVersion = AppVersion.Loading,
            loginEffect = Effect.empty()
        )
    }
}
