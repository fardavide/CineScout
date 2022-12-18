package cinescout.details.presentation.state

import cinescout.design.model.ConnectionStatusUiModel

data class TvShowDetailsState(
    val tvShowState: TvShowDetailsTvShowState,
    val connectionStatus: ConnectionStatusUiModel
) {

    companion object {

        val Loading = TvShowDetailsState(
            tvShowState = TvShowDetailsTvShowState.Loading,
            connectionStatus = ConnectionStatusUiModel.AllConnected
        )
    }
}
