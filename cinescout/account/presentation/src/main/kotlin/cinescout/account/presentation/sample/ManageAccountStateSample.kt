package cinescout.account.presentation.sample

import cinescout.account.presentation.state.ManageAccountState
import cinescout.design.testdata.MessageSample
import cinescout.design.util.Effect

object ManageAccountStateSample {

    val Error = ManageAccountState(
        account = Account.Error,
        loginEffect = Effect.empty()
    )

    val Loading = ManageAccountState(account = ManageAccountState.Account.Loading, loginEffect = Effect.empty())

    val NotConnected = ManageAccountState(
        account = Account.NotConnected,
        loginEffect = Effect.empty()
    )

    val TmdbConnected = ManageAccountState(
        account = Account.TmdbConnected,
        loginEffect = Effect.empty()
    )

    val TraktConnected = ManageAccountState(
        account = Account.TraktConnected,
        loginEffect = Effect.empty()
    )

    object Account {

        val Error = ManageAccountState.Account.Error(
            message = MessageSample.NoNetworkError
        )

        val Loading = ManageAccountState.Account.Loading

        val NotConnected = ManageAccountState.Account.NotConnected

        val TmdbConnected = ManageAccountState.Account.Connected(
            uiModel = AccountUiModelSample.Tmdb
        )

        val TraktConnected = ManageAccountState.Account.Connected(
            uiModel = AccountUiModelSample.Trakt
        )
    }
}
