package cinescout.profile.presentation.sample

import cinescout.design.testdata.MessageSample
import cinescout.profile.presentation.state.ProfileState

internal object ProfileStateSample {

    val AccountConnected = ProfileState(
        account = ProfileState.Account.Connected
    )

    val AccountError = ProfileState(
        account = ProfileState.Account.Error(
            message = MessageSample.NoNetworkError
        )
    )

    val AccountNotConnected = ProfileState(
        account = ProfileState.Account.NotConnected
    )
}
