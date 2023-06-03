package cinescout.details.presentation.previewdata

import cinescout.design.model.ConnectionStatusUiModel
import cinescout.design.util.PreviewDataProvider
import cinescout.details.presentation.sample.DetailActionsUiModelSample
import cinescout.details.presentation.sample.ScreenplayDetailsUiModelSample
import cinescout.details.presentation.state.ScreenplayDetailsItemState
import cinescout.details.presentation.state.ScreenplayDetailsState
import cinescout.resources.R.string
import cinescout.resources.TextRes

internal object ScreenplayDetailsScreenPreviewData {

    val Inception = ScreenplayDetailsState(
        actionsUiModel = DetailActionsUiModelSample.AllOn,
        connectionStatus = ConnectionStatusUiModel.AllConnected,
        itemState = ScreenplayDetailsItemState.Data(
            ScreenplayDetailsUiModelSample.Inception
        )
    )
    val Loading = ScreenplayDetailsState(
        actionsUiModel = DetailActionsUiModelSample.AllOff,
        connectionStatus = ConnectionStatusUiModel.AllConnected,
        itemState = ScreenplayDetailsItemState.Loading
    )
    val TheWolfOfWallStreet = ScreenplayDetailsState(
        actionsUiModel = DetailActionsUiModelSample.AllOn,
        connectionStatus = ConnectionStatusUiModel.AllConnected,
        itemState = ScreenplayDetailsItemState.Data(
            ScreenplayDetailsUiModelSample.TheWolfOfWallStreet
        )
    )
    val TmdbOffline = ScreenplayDetailsState(
        actionsUiModel = DetailActionsUiModelSample.AllOff,
        connectionStatus = ConnectionStatusUiModel.TmdbOffline,
        itemState = ScreenplayDetailsItemState.Error(TextRes(string.network_error_no_network))
    )
}

internal class ScreenplayDetailsScreenPreviewDataProvider : PreviewDataProvider<ScreenplayDetailsState>(
    ScreenplayDetailsScreenPreviewData.Inception,
    ScreenplayDetailsScreenPreviewData.TheWolfOfWallStreet,
    ScreenplayDetailsScreenPreviewData.TmdbOffline,
    ScreenplayDetailsScreenPreviewData.Loading
)
