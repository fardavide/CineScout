package cinescout.home.presentation.sample

import cinescout.account.domain.model.Gravatar
import cinescout.account.domain.sample.AccountSample
import cinescout.design.TextRes
import cinescout.design.model.ConnectionStatusUiModel
import cinescout.design.util.Effect
import cinescout.home.presentation.model.AccountUiModel
import cinescout.home.presentation.state.HomeState
import cinescout.unsupported

object HomeStateSample {

    val TmdbAccount = HomeState.Account.Connected(
        AccountUiModel(
            username = AccountSample.Tmdb.username.value,
            imageUrl = AccountSample.Tmdb.gravatar?.getUrl(Gravatar.Size.SMALL)
        )
    )

    val TraktAccount = HomeState.Account.Connected(
        AccountUiModel(
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
            appVersion = HomeState.AppVersion.Data(123),
            loginEffect = Effect.empty(),
            connectionStatus = ConnectionStatusUiModel.AllConnected
        )

        var account: HomeState.Account
            get() = unsupported
            set(value) {
                state = state.copy(account = value)
            }

        var appVersion: HomeState.AppVersion
            get() = state.appVersion
            set(value) {
                state = state.copy(appVersion = value)
            }

        var appVersionInt: Int
            get() = unsupported
            set(value) {
                appVersion = HomeState.AppVersion.Data(value)
            }

        var login: HomeState.Login
            get() = unsupported
            set(value) {
                state = state.copy(loginEffect = Effect.of(value))
            }

        var networkStatus: ConnectionStatusUiModel
            get() = state.connectionStatus
            set(value) {
                state = state.copy(connectionStatus = value)
            }

        infix fun TextRes.`as`(@Suppress("UNUSED_PARAMETER") loginError: LoginError) =
            HomeState.Login.Error(this)

        object LoginError
    }

    @DslMarker
    annotation class HomeStateDsl
}
