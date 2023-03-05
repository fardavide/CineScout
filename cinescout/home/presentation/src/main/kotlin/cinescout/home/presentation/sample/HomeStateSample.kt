package cinescout.home.presentation.sample

import cinescout.account.domain.model.Gravatar
import cinescout.account.domain.sample.AccountSample
import cinescout.design.model.ConnectionStatusUiModel
import cinescout.home.presentation.model.HomeAccountUiModel
import cinescout.home.presentation.state.HomeState
import cinescout.unsupported

object HomeStateSample {

    val Account = HomeState.Account.Connected(
        HomeAccountUiModel(
            username = AccountSample.Trakt.username.value,
            imageUrl = AccountSample.Trakt.gravatar?.getUrl(Gravatar.Size.SMALL)
        )
    )

    @HomeStateDsl
    fun buildHomeState(block: HomeStateBuilder.() -> Unit = {}): HomeState =
        HomeStateBuilder().apply(block).state

    @HomeStateDsl
    class HomeStateBuilder {

        internal var state: HomeState = HomeState(
            account = HomeState.Account.NotConnected,
            connectionStatus = ConnectionStatusUiModel.AllConnected
        )

        var account: HomeState.Account
            get() = unsupported
            set(value) {
                state = state.copy(account = value)
            }

        var networkStatus: ConnectionStatusUiModel
            get() = state.connectionStatus
            set(value) {
                state = state.copy(connectionStatus = value)
            }
    }

    @DslMarker
    annotation class HomeStateDsl
}
