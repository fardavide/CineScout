package cinescout.profile.presentation.sample

import cinescout.profile.presentation.state.ProfileState

internal object ProfileStateSample {

    val AccountConnected = ProfileState(
        account = ProfileState.Account.Connected(
            uiModel = ProfileAccountUiModelSample.Account
        )
    )

    val AccountError = ProfileState(
        account = ProfileState.Account.Error
    )

    val AccountNotConnected = ProfileState(
        account = ProfileState.Account.NotConnected
    )
}
