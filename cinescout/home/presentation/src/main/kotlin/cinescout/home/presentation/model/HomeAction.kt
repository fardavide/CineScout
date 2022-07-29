package cinescout.home.presentation.model

sealed interface HomeAction {

    object LoginToTmdb : HomeAction
    object LoginToTrakt : HomeAction
    object NotifyTmdbAppAuthorized : HomeAction
    object NotifyTraktAppAuthorized : HomeAction
}
