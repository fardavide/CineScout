package cinescout.home.presentation.model

import cinescout.auth.trakt.domain.model.TraktAuthorizationCode

sealed interface HomeAction {

    object LoginToTmdb : HomeAction
    object LoginToTrakt : HomeAction
    object LogoutFromTmdb : HomeAction
    object NotifyTmdbAppAuthorized : HomeAction
    data class NotifyTraktAppAuthorized(val code: TraktAuthorizationCode) : HomeAction
}
