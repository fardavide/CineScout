package cinescout.home.presentation.viewmodel

import androidx.lifecycle.viewModelScope
import cinescout.GetAppVersion
import cinescout.account.domain.model.GetAccountError
import cinescout.account.domain.model.Gravatar
import cinescout.account.domain.usecase.GetTmdbAccount
import cinescout.account.domain.usecase.GetTraktAccount
import cinescout.design.NetworkErrorToMessageMapper
import cinescout.design.model.ConnectionStatusUiModel
import cinescout.home.presentation.action.HomeAction
import cinescout.home.presentation.state.HomeState
import cinescout.network.model.ConnectionStatus
import cinescout.network.usecase.ObserveConnectionStatus
import cinescout.utils.android.CineScoutViewModel
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.launch
import org.koin.android.annotation.KoinViewModel
import store.Refresh
import kotlin.time.Duration.Companion.seconds

@KoinViewModel
internal class HomeViewModel(
    private val getAppVersion: GetAppVersion,
    private val getTmdbAccount: GetTmdbAccount,
    private val getTraktAccount: GetTraktAccount,
    private val networkErrorMapper: NetworkErrorToMessageMapper,
    private val observeConnectionStatus: ObserveConnectionStatus
) : CineScoutViewModel<HomeAction, HomeState>(initialState = HomeState.Loading) {

    init {
        updateState { currentState ->
            currentState.copy(appVersion = HomeState.AppVersion.Data(getAppVersion()))
        }
        viewModelScope.launch {
            observeConnectionStatus().collectLatest { connectionStatus ->
                updateState { currentState ->
                    currentState.copy(connectionStatus = connectionStatus.toUiModel())
                }
            }
        }
        viewModelScope.launch {
            combine(
                getTmdbAccount(Refresh.WithInterval(3.seconds)),
                getTraktAccount(Refresh.WithInterval(3.seconds))
            ) { tmdbAccountEither, traktAccountEither ->
                val newTmdbAccount = tmdbAccountEither.fold(
                    ifLeft = { error -> toAccountState(error) },
                    ifRight = { account ->
                        HomeState.Accounts.Account.Data(
                            imageUrl = account.gravatar?.getUrl(Gravatar.Size.SMALL),
                            username = account.username.value
                        )
                    }
                )
                val newTraktAccount = traktAccountEither.fold(
                    ifLeft = { error -> toAccountState(error) },
                    ifRight = { account ->
                        HomeState.Accounts.Account.Data(
                            imageUrl = account.gravatar?.getUrl(Gravatar.Size.SMALL),
                            username = account.username.value
                        )
                    }
                )
                val newPrimaryAccount = newTraktAccount as? HomeState.Accounts.Account.Data
                    ?: newTmdbAccount as? HomeState.Accounts.Account.Data
                    ?: HomeState.Accounts.Account.NoAccountConnected
                HomeState.Accounts(
                    primary = newPrimaryAccount,
                    tmdb = newTmdbAccount,
                    trakt = newTraktAccount
                )
            }.collect { accounts ->
                updateState { currentState ->
                    currentState.copy(accounts = accounts)
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

    private fun toAccountState(error: GetAccountError): HomeState.Accounts.Account = when (error) {
        is GetAccountError.Network ->
            HomeState.Accounts.Account.Error(networkErrorMapper.toMessage(error.networkError))
        GetAccountError.NoAccountConnected -> HomeState.Accounts.Account.NoAccountConnected
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
