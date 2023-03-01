package cinescout.profile.presentation.state

import cinescout.profile.presentation.model.ProfileAccountUiModel

internal data class ProfileState(
    val account: Account
) {

    sealed interface Account {
        data class Connected(val uiModel: ProfileAccountUiModel) : Account
        object Error : Account
        object Loading : Account
        object NotConnected : Account
    }

    companion object {

        val Loading = ProfileState(
            account = Account.Loading
        )
    }
}
