package cinescout.home.presentation.state

import cinescout.design.model.ConnectionStatusUiModel
import cinescout.home.presentation.model.HomeAccountUiModel
import cinescout.resources.TextRes

data class HomeState(
    val account: Account,
    val connectionStatus: ConnectionStatusUiModel
) {

    sealed interface Account {
        data class Connected(val uiModel: HomeAccountUiModel) : Account
        data class Error(val message: TextRes) : Account
        object Loading : Account
        object NotConnected : Account
    }

    companion object {

        val Loading = HomeState(
            account = Account.Loading,
            connectionStatus = ConnectionStatusUiModel.AllConnected
        )
    }
}
