package cinescout.home.presentation.model

import cinescout.design.TextRes

sealed interface ManageAccountState {
    data class Connected(val uiModel: AccountUiModel) : ManageAccountState
    data class Error(val message: TextRes) : ManageAccountState
    object Loading : ManageAccountState
    object NotConnected : ManageAccountState
}
