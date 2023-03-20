package cinescout.account.presentation.sample

import cinescout.account.presentation.state.ManageAccountState
import cinescout.resources.sample.MessageSample
import cinescout.utils.compose.Effect

object ManageAccountStateSample {

    val Connected = ManageAccountState(
        account = Account.Connected,
        loginEffect = Effect.empty()
    )

    val Error = ManageAccountState(
        account = Account.Error,
        loginEffect = Effect.empty()
    )

    val Loading = ManageAccountState(account = ManageAccountState.Account.Loading, loginEffect = Effect.empty())

    val NotConnected = ManageAccountState(
        account = Account.NotConnected,
        loginEffect = Effect.empty()
    )

    object Account {

        val Connected = ManageAccountState.Account.Connected(
            uiModel = AccountUiModelSample.Trakt
        )

        val Error = ManageAccountState.Account.Error(
            message = MessageSample.NoNetworkError
        )

        val NotConnected = ManageAccountState.Account.NotConnected
    }
}
