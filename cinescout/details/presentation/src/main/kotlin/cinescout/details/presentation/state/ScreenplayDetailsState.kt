package cinescout.details.presentation.state

import cinescout.design.model.ConnectionStatusUiModel
import cinescout.details.presentation.model.DetailsActionsUiModel

internal data class ScreenplayDetailsState(
    val actionsUiModel: DetailsActionsUiModel,
    val connectionStatus: ConnectionStatusUiModel,
    val itemState: ScreenplayDetailsItemState
)
