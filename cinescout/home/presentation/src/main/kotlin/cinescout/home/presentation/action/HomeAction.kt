package cinescout.home.presentation.action

import cinescout.auth.trakt.domain.model.TraktAuthorizationCode

sealed interface HomeAction {

    object LoginToTmdb : HomeAction
    object LoginToTrakt : HomeAction
    object LogoutFromTmdb : HomeAction
    object LogoutFromTrakt : HomeAction
    object NotifyTmdbAppAuthorized : HomeAction
    data class NotifyTraktAppAuthorized(val code: TraktAuthorizationCode) : HomeAction
}
