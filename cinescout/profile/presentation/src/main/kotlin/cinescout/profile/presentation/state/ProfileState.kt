package cinescout.profile.presentation.state

import cinescout.design.TextRes

internal data class ProfileState(
    val account: Account
) {

    sealed interface Account {
        object Connected : Account
        data class Error(val message: TextRes) : Account
        object Loading : Account
        object NotConnected : Account
    }

    companion object {

        val Loading = ProfileState(
            account = Account.Loading
        )
    }
}
