package cinescout.profile.presentation.sample

import cinescout.profile.presentation.state.ProfileState

internal object ProfileStateSample {

    val AccountConnected = ProfileState(
        account = ProfileState.Account.Connected(
            uiModel = ProfileAccountUiModelSample.Account
        ),
        appVersion = 123
    )

    val AccountError = ProfileState(
        account = ProfileState.Account.Error,
        appVersion = 123
    )

    val AccountNotConnected = ProfileState(
        account = ProfileState.Account.NotConnected,
        appVersion = 123
    )
}
