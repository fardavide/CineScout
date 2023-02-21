package cinescout.home.presentation.sample

import cinescout.account.domain.model.Gravatar
import cinescout.account.domain.sample.AccountSample
import cinescout.design.TextRes
import cinescout.design.model.ConnectionStatusUiModel
import cinescout.design.util.Effect
import cinescout.home.presentation.state.HomeState
import cinescout.unsupported

object HomeStateSample {

    val TmdbAccount = HomeState.Accounts.Account.Data(
        username = AccountSample.Tmdb.username.value,
        imageUrl = AccountSample.Tmdb.gravatar?.getUrl(Gravatar.Size.SMALL)
    )

    val TraktAccount = HomeState.Accounts.Account.Data(
        username = AccountSample.Trakt.username.value,
        imageUrl = AccountSample.Trakt.gravatar?.getUrl(Gravatar.Size.SMALL)
    )

    @HomeStateDsl
    fun buildHomeState(block: HomeStateBuilder.() -> Unit = {}): HomeState =
        HomeStateBuilder().apply(block).state

    @HomeStateDsl
    class HomeStateBuilder {

        internal var state: HomeState = HomeState(
            accounts = HomeState.Accounts(
                primary = HomeState.Accounts.Account.NoAccountConnected,
                tmdb = HomeState.Accounts.Account.NoAccountConnected,
                trakt = HomeState.Accounts.Account.NoAccountConnected
            ),
            appVersion = HomeState.AppVersion.Data(123),
            loginEffect = Effect.empty(),
            connectionStatus = ConnectionStatusUiModel.AllConnected
        )

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

        @HomeStateDsl
        fun accounts(block: AccountsBuilder.() -> Unit) {
            state = state.copy(accounts = AccountsBuilder().apply(block).accounts)
        }

        infix fun TextRes.`as`(@Suppress("UNUSED_PARAMETER") loginError: LoginError) =
            HomeState.Login.Error(this)

        object LoginError
    }

    @HomeStateDsl
    class AccountsBuilder {

        internal var accounts: HomeState.Accounts = HomeState.Accounts(
            primary = HomeState.Accounts.Account.NoAccountConnected,
            tmdb = HomeState.Accounts.Account.NoAccountConnected,
            trakt = HomeState.Accounts.Account.NoAccountConnected
        )

        var tmdb: HomeState.Accounts.Account
            get() = accounts.tmdb
            set(value) {
                val primary = if (accounts.primary.isData().not() && value.isData()) value else accounts.primary
                accounts = accounts.copy(primary = primary, tmdb = value)
            }

        var trakt: HomeState.Accounts.Account
            get() = accounts.trakt
            set(value) {
                val primary = if (accounts.primary.isData().not() && value.isData()) value else accounts.primary
                accounts = accounts.copy(primary = primary, trakt = value)
            }

        infix fun HomeState.Accounts.Account.`as`(
            @Suppress("UNUSED_PARAMETER") primary: Primary
        ): HomeState.Accounts.Account {
            accounts = accounts.copy(primary = this)
            return this
        }

        infix fun TextRes.`as`(@Suppress("UNUSED_PARAMETER") accountError: AccountError) =
            HomeState.Accounts.Account.Error(this)

        object AccountError
        object Primary
    }

    @DslMarker
    annotation class HomeStateDsl
}

private fun HomeState.Accounts.Account.isData() = this is HomeState.Accounts.Account.Data
