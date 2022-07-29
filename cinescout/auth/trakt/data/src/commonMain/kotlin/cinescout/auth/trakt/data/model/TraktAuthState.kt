package cinescout.auth.trakt.data.model

import cinescout.auth.trakt.domain.model.TraktAuthorizationCode

sealed interface TraktAuthState {

    object Idle : TraktAuthState

    data class AppAuthorized(val code: TraktAuthorizationCode) : TraktAuthState

    data class Completed(val tokens: TraktAccessAndRefreshTokens) : TraktAuthState
}
