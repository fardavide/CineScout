package cinescout.details.presentation.mapper

import cinescout.design.model.ConnectionStatusUiModel
import cinescout.network.model.ConnectionStatus

internal fun ConnectionStatus.toUiModel(): ConnectionStatusUiModel = when {
    device == ConnectionStatus.Connection.Offline -> ConnectionStatusUiModel.DeviceOffline
    tmdb == ConnectionStatus.Connection.Offline && trakt == ConnectionStatus.Connection.Offline ->
        ConnectionStatusUiModel.TmdbAndTraktOffline
    tmdb == ConnectionStatus.Connection.Offline -> ConnectionStatusUiModel.TmdbOffline
    trakt == ConnectionStatus.Connection.Offline -> ConnectionStatusUiModel.TraktOffline
    else -> ConnectionStatusUiModel.AllConnected
}
