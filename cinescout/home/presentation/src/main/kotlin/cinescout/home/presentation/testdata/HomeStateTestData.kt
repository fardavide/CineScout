package cinescout.home.presentation.testdata

import cinescout.design.TextRes
import cinescout.design.util.Effect
import cinescout.home.presentation.model.HomeState
import cinescout.unsupported

object HomeStateTestData {

    @HomeStateDsl
    fun buildHomeState(block: HomeStateBuilder.() -> Unit): HomeState =
        HomeStateBuilder().apply(block).state

    @HomeStateDsl
    class HomeStateBuilder {

        internal var state: HomeState = HomeState.Loading

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

        @HomeStateDsl
        fun accounts(block: AccountsBuilder.() -> Unit) {
            state = state.copy(accounts = AccountsBuilder().apply(block).accounts)
        }

        infix fun TextRes.`as`(
            @Suppress("UNUSED_PARAMETER") loginError: LoginError
        ) = HomeState.Login.Error(this)

        object LoginError
    }

    @HomeStateDsl
    class AccountsBuilder {

        internal var accounts: HomeState.Accounts = HomeState.Accounts(
            primary = HomeState.Accounts.Account.Loading,
            tmdb = HomeState.Accounts.Account.Loading,
            trakt = HomeState.Accounts.Account.Loading
        )

        var tmdb: HomeState.Accounts.Account
            get() = accounts.tmdb
            set(value) {
                val primary = when (val primary = accounts.primary) {
                    HomeState.Accounts.Account.Loading -> value
                    else -> primary
                }
                accounts = accounts.copy(primary = primary, tmdb = value)
            }

        var trakt: HomeState.Accounts.Account
            get() = accounts.trakt
            set(value) {
                val primary = when (val primary = accounts.primary) {
                    HomeState.Accounts.Account.Loading -> value
                    else -> primary
                }
                accounts = accounts.copy(primary = primary, trakt = value)
            }

        infix fun HomeState.Accounts.Account.`as`(
            @Suppress("UNUSED_PARAMETER") primary: Primary
        ): HomeState.Accounts.Account {
            accounts = accounts.copy(primary = this)
            return this
        }

        infix fun TextRes.`as`(
            @Suppress("UNUSED_PARAMETER") accountError: AccountError
        ) = HomeState.Accounts.Account.Error(this)

        object AccountError
        object Primary
    }

    @DslMarker
    annotation class HomeStateDsl
}
