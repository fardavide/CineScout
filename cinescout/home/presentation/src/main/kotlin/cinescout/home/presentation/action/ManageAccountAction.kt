package cinescout.home.presentation.action

import cinescout.auth.trakt.domain.model.TraktAuthorizationCode

sealed interface ManageAccountAction {

    object LinkToTmdb : ManageAccountAction
    object LinkToTrakt : ManageAccountAction
    object NotifyTmdbAppAuthorized : ManageAccountAction
    data class NotifyTraktAppAuthorized(val code: TraktAuthorizationCode) : ManageAccountAction
    object UnlinkFromTmdb : ManageAccountAction
    object UnlinkFromTrakt : ManageAccountAction
}
