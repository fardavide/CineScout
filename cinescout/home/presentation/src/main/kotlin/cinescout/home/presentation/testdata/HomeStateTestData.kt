package cinescout.home.presentation.testdata

import cinescout.design.TextRes
import cinescout.design.util.Effect
import cinescout.home.presentation.model.HomeState

object HomeStateTestData {

    fun buildHomeState(
        account: HomeState.Account = HomeState.Account.Loading,
        login: Effect<HomeState.Login> = Effect.empty()
    ) = HomeState(account, login)

    fun buildHomeState(
        accountErrorText: TextRes,
        login: Effect<HomeState.Login> = Effect.empty()
    ) = HomeState(HomeState.Account.Error(accountErrorText), login)

    fun buildHomeState(
        account: HomeState.Account = HomeState.Account.Loading,
        loginErrorText: TextRes
    ) = HomeState(account, Effect.of(HomeState.Login.Error(loginErrorText)))
}
