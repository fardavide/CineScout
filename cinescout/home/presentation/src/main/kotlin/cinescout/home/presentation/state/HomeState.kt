package cinescout.home.presentation.state

import cinescout.design.TextRes
import cinescout.design.model.ConnectionStatusUiModel
import cinescout.design.util.Effect
import cinescout.home.presentation.model.AccountUiModel

data class HomeState(
    val account: Account,
    val appVersion: AppVersion,
    val loginEffect: Effect<Login>,
    val connectionStatus: ConnectionStatusUiModel
) {

    sealed interface Account {
        data class Connected(val uiModel: AccountUiModel) : Account
        data class Error(val message: TextRes) : Account
        object Loading : Account
        object NotConnected : Account
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
            account = Account.Loading,
            appVersion = AppVersion.Loading,
            loginEffect = Effect.empty(),
            connectionStatus = ConnectionStatusUiModel.AllConnected
        )
    }
}
