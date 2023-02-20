package cinescout.home.presentation.sample

import cinescout.design.testdata.MessageSample
import cinescout.home.presentation.model.ManageAccountState

object ManageAccountStateSample {

    val Error = ManageAccountState.Error(
        message = MessageSample.NoNetworkError
    )

    val Loading = ManageAccountState.Loading

    val NotConnected = ManageAccountState.NotConnected

    val TmdbConnected = ManageAccountState.Connected(
        uiModel = AccountUiModelSample.Tmdb
    )

    val TraktConnected = ManageAccountState.Connected(
        uiModel = AccountUiModelSample.Trakt
    )
}
