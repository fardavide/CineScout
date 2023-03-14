package cinescout.details.presentation.state

import cinescout.design.model.ConnectionStatusUiModel

data class ScreenplayDetailsState(
    val itemState: ScreenplayDetailsItemState,
    val connectionStatus: ConnectionStatusUiModel
) {

    companion object {

        val Loading = ScreenplayDetailsState(
            itemState = ScreenplayDetailsItemState.Loading,
            connectionStatus = ConnectionStatusUiModel.AllConnected
        )
    }
}
