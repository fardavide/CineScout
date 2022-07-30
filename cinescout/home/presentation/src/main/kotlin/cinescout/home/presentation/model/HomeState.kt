package cinescout.home.presentation.model

import cinescout.design.TextRes
import cinescout.design.util.Effect

data class HomeState(
    val loginStateEffect: Effect<LoginState>
) {

    sealed interface LoginState {
        data class Error(val message: TextRes) : LoginState
        object Linked : LoginState
        data class UserShouldAuthorizeApp(val authorizationUrl: String) : LoginState
    }

    companion object {

        val Idle = HomeState(
            loginStateEffect = Effect.empty()
        )
    }
}
