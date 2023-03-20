package cinescout.account.presentation.state

import cinescout.account.presentation.model.AccountUiModel
import cinescout.design.util.Effect
import cinescout.resources.TextRes

data class ManageAccountState(
    val account: Account,
    val loginEffect: Effect<Login>
) {

    sealed interface Account {
        data class Connected(val uiModel: AccountUiModel) : Account
        data class Error(val message: TextRes) : Account
        object Loading : Account
        object NotConnected : Account
    }

    sealed interface Login {
        data class Error(val message: TextRes) : Login
        object Linked : Login
        data class UserShouldAuthorizeApp(val authorizationUrl: String) : Login
    }

    companion object {

        val Loading = ManageAccountState(
            account = Account.Loading,
            loginEffect = Effect.empty()
        )
    }
}
