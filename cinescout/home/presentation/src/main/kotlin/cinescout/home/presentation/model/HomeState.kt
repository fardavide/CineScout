package cinescout.home.presentation.model

import cinescout.design.TextRes

sealed interface HomeState {

    data class Error(val message: TextRes) : HomeState
    object Idle : HomeState
    object Linked : HomeState
    data class UserShouldAuthorizeApp(val authorizationUrl: String) : HomeState
}
