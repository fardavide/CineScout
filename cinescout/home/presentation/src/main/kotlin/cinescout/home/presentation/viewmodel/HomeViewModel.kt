package cinescout.home.presentation.viewmodel

import androidx.lifecycle.viewModelScope
import cinescout.account.domain.model.GetAccountError
import cinescout.account.domain.model.Gravatar
import cinescout.account.domain.usecase.GetCurrentAccount
import cinescout.design.model.ConnectionStatusUiModel
import cinescout.home.presentation.action.HomeAction
import cinescout.home.presentation.model.HomeAccountUiModel
import cinescout.home.presentation.state.HomeState
import cinescout.network.model.ConnectionStatus
import cinescout.network.usecase.ObserveConnectionStatus
import cinescout.utils.android.CineScoutViewModel
import cinescout.utils.compose.NetworkErrorToMessageMapper
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import org.koin.android.annotation.KoinViewModel

@KoinViewModel
internal class HomeViewModel(
    private val getCurrentAccount: GetCurrentAccount,
    private val networkErrorMapper: NetworkErrorToMessageMapper,
    private val observeConnectionStatus: ObserveConnectionStatus
) : CineScoutViewModel<HomeAction, HomeState>(initialState = HomeState.Loading) {

    init {
        viewModelScope.launch {
            observeConnectionStatus().collectLatest { connectionStatus ->
                updateState { currentState ->
                    currentState.copy(connectionStatus = connectionStatus.toUiModel())
                }
            }
        }
        viewModelScope.launch {
            getCurrentAccount(refresh = true).map { accountEither ->
                accountEither.fold(
                    ifLeft = { error -> toAccountState(error) },
                    ifRight = { account ->
                        val uiModel = HomeAccountUiModel(
                            imageUrl = account.gravatar?.getUrl(Gravatar.Size.SMALL),
                            username = account.username.value
                        )
                        HomeState.Account.Connected(uiModel)
                    }
                )
            }.collectLatest { account ->
                updateState { currentState ->
                    currentState.copy(account = account)
                }
            }
        }
    }

    override fun submit(action: HomeAction) {
        when (action) {
            HomeAction.Empty -> {
                // no-op
            }
        }
    }

    private fun toAccountState(error: GetAccountError): HomeState.Account = when (error) {
        is GetAccountError.Network ->
            HomeState.Account.Error(networkErrorMapper.toMessage(error.networkError))
        GetAccountError.NotConnected -> HomeState.Account.NotConnected
    }
}

private fun ConnectionStatus.toUiModel(): ConnectionStatusUiModel = when {
    device == ConnectionStatus.Connection.Offline -> ConnectionStatusUiModel.DeviceOffline
    tmdb == ConnectionStatus.Connection.Offline && trakt == ConnectionStatus.Connection.Offline ->
        ConnectionStatusUiModel.TmdbAndTraktOffline

    tmdb == ConnectionStatus.Connection.Offline -> ConnectionStatusUiModel.TmdbOffline
    trakt == ConnectionStatus.Connection.Offline -> ConnectionStatusUiModel.TraktOffline
    else -> ConnectionStatusUiModel.AllConnected
}
