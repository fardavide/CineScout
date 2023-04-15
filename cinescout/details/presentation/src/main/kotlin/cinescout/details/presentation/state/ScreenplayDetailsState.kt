package cinescout.details.presentation.state

import cinescout.design.model.ConnectionStatusUiModel

data class ScreenplayDetailsState(
    val connectionStatus: ConnectionStatusUiModel,
    val itemState: ScreenplayDetailsItemState
) {

    companion object {

        val Loading = ScreenplayDetailsState(
            connectionStatus = ConnectionStatusUiModel.AllConnected,
            itemState = ScreenplayDetailsItemState.Loading
        )
    }
}
