package cinescout.account.presentation.action

import cinescout.auth.domain.model.TraktAuthorizationCode

sealed interface ManageAccountAction {

    object LinkToTrakt : ManageAccountAction
    data class NotifyTraktAppAuthorized(val code: TraktAuthorizationCode) : ManageAccountAction
    object UnlinkFromTrakt : ManageAccountAction
}
