package cinescout.home.presentation.testdata

import cinescout.design.TextRes
import cinescout.design.util.Effect
import cinescout.home.presentation.model.HomeState

object HomeStateTestData {

    fun buildHomeState(
        account: HomeState.Accounts.Account = HomeState.Accounts.Account.Loading,
        appVersion: HomeState.AppVersion = HomeState.AppVersion.Loading,
        login: Effect<HomeState.Login> = Effect.empty()
    ) = HomeState(
        account = account,
        appVersion = appVersion,
        loginEffect = login
    )

    fun buildHomeState(
        accountErrorText: TextRes,
        appVersion: HomeState.AppVersion = HomeState.AppVersion.Loading,
        login: Effect<HomeState.Login> = Effect.empty()
    ) = HomeState(
        account = HomeState.Accounts.Account.Error(accountErrorText),
        appVersion = appVersion,
        loginEffect = login
    )
    fun buildHomeState(
        accountErrorText: TextRes,
        appVersionInt: Int,
        login: Effect<HomeState.Login> = Effect.empty()
    ) = HomeState(
        account = HomeState.Accounts.Account.Error(accountErrorText),
        appVersion = HomeState.AppVersion.Data(appVersionInt),
        loginEffect = login
    )

    fun buildHomeState(
        account: HomeState.Accounts.Account = HomeState.Accounts.Account.Loading,
        appVersionInt: Int,
        login: Effect<HomeState.Login> = Effect.empty()
    ) = HomeState(
        account = account,
        appVersion = HomeState.AppVersion.Data(appVersionInt),
        loginEffect = login
    )

    fun buildHomeState(
        account: HomeState.Accounts.Account = HomeState.Accounts.Account.Loading,
        appVersion: HomeState.AppVersion = HomeState.AppVersion.Loading,
        loginErrorText: TextRes
    ) = HomeState(
        account = account,
        appVersion = appVersion,
        loginEffect = Effect.of(HomeState.Login.Error(loginErrorText))
    )

    fun buildHomeState(
        account: HomeState.Accounts.Account = HomeState.Accounts.Account.Loading,
        appVersionInt: Int,
        loginErrorText: TextRes
    ) = HomeState(
        account = account,
        appVersion = HomeState.AppVersion.Data(appVersionInt),
        loginEffect = Effect.of(HomeState.Login.Error(loginErrorText))
    )
}
